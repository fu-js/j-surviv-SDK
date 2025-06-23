package jsclub.codefest.sdk;

import io.socket.emitter.Emitter;
import jsclub.codefest.sdk.algorithm.PathUtils;
import jsclub.codefest.sdk.base.Node;
import jsclub.codefest.sdk.model.GameMap;
import jsclub.codefest.sdk.model.players.Player;
import jsclub.codefest.sdk.model.weapon.Weapon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final String SERVER_URL = "https://cf25-server-staging.jsclub.dev";
    private static final String GAME_ID = "120154";
    private static final String PLAYER_NAME = "lily";
    private static final String SECRET_KEY = "sk-7YLsiWMySB-d_CgInug_mw:kdeAyOViSRxixQCj4VgYfiT1TJpg5AVbUgrcNonekN0ib9zSHrDTO-L3nKc9GKA40rlFjBT10C_dftiVuMaj1Q";

    public static final int STUCK_LIMIT = 4;
    public static final int DODGE_RANGE = 3;

    public static void main(String[] args) throws IOException {
        Hero hero = new Hero(GAME_ID, PLAYER_NAME, SECRET_KEY);
        Emitter.Listener onMapUpdate = new MapUpdateListener(hero);

        hero.setOnMapUpdate(onMapUpdate);
        hero.start(SERVER_URL);
    }
}

class MapUpdateListener implements Emitter.Listener {
    private final Hero hero;
    private int stuckCounter = 0;
    private Node lastPosition = new Node(-1, -1);

    public MapUpdateListener(Hero hero) {
        this.hero = hero;
    }

    @Override
    public void call(Object... args) {
        try {
            if (args == null || args.length == 0) return;

            GameMap gameMap = hero.getGameMap();
            gameMap.updateOnUpdateMap(args[0]);
            Player player = gameMap.getCurrentPlayer();

            if (player == null || player.getHealth() == 0) {
                System.out.println("Player is dead or data is not available.");
                return;
            }

            System.out.println("CurrentPlayer: " + player.getPosition());
            System.out.println("CurrentInventory: " + player.getInventory());

            handleStuckDetection(player);
            if (stuckCounter > Main.STUCK_LIMIT) {
                handleStuck();
                return;
            }

            List<Node> nodesToAvoid = getNodesToAvoid(gameMap);
            Player nearestPlayer = getNearestPlayer(gameMap, player);

            if (player.getInventory().getGun() == null) {
                handleSearchForGun(gameMap, player, nodesToAvoid);
            } else {
                handleCombat(nearestPlayer, nodesToAvoid, player);
            }

            System.out.println("game map: "+ gameMap);


        } catch (Exception e) {
            System.err.println("Critical error in call method: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleStuckDetection(Player player) {
        if (isStuck(player)) {
            stuckCounter++;
            System.out.println("Detected stuck: " + stuckCounter + "/" + Main.STUCK_LIMIT);
        } else {
            stuckCounter = 0;
        }
        updateLastPosition(player);
    }

    private void handleStuck() throws IOException {
        System.out.println("Stuck! Attempting random movement.");
        hero.move(getRandomDirection());
        stuckCounter = 0;
    }

    private boolean shouldDodge(Player nearestPlayer, Player player) {
        return player.getInventory().getGun() == null &&
                nearestPlayer != null &&
                PathUtils.distance(player, nearestPlayer) <= Main.DODGE_RANGE;
    }

    private void handleDodge(Player nearestPlayer, List<Node> nodesToAvoid, Player player) throws IOException {
        System.out.println("Enemy too close! Attempting to dodge.");
        String dodgeDirection = findDodgeDirection(nodesToAvoid, player, nearestPlayer);
        if (dodgeDirection != null) {
            System.out.println("Dodging in direction: " + dodgeDirection);
            hero.move(dodgeDirection);
        } else {
            System.out.println("No safe dodge found, moving randomly.");
            hero.move(getRandomDirection());
        }
    }

    private void handleSearchForGun(GameMap gameMap, Player player, List<Node> nodesToAvoid) throws IOException {
        System.out.println("No gun found. Searching for a gun.");
        String pathToGun = findPathToGun(gameMap, nodesToAvoid, player);

        if (pathToGun != null) {
            if (pathToGun.isEmpty()) {
                hero.pickupItem();
            } else {
                hero.move(pathToGun);
            }
        } else {
            hero.move(getRandomDirection());
        }
    }

    private void handleCombat(Player nearestPlayer, List<Node> nodesToAvoid, Player player) throws IOException {
        if (nearestPlayer == null) {
            hero.move(getRandomDirection());
            return;
        }

        String pathToEnemy = findPathToOtherPlayer(nodesToAvoid, player, nearestPlayer);
        if (pathToEnemy != null && PathUtils.distance(player, nearestPlayer) <= player.getInventory().getGun().getRange()) {
            System.out.println("Enemy in range. Shooting!");
            hero.shoot(pathToEnemy.substring(0, 1));
        } else if (pathToEnemy != null) {
            System.out.println("Moving closer to enemy: " + pathToEnemy);
            hero.move(pathToEnemy);
        } else {
            hero.move(getRandomDirection());
        }
    }

    private boolean isStuck(Player player) {
        return player.x == lastPosition.x && player.y == lastPosition.y;
    }

    private void updateLastPosition(Player player) {
        lastPosition.setPosition(player.x, player.y);
    }

    private String findDodgeDirection(List<Node> nodesToAvoid, Player player, Player nearestEnemy) {
        String[] directions = {"u", "d", "l", "r"};
        Node currentPlayerPos = new Node(player.x, player.y);

        for (String dir : directions) {
            Node nextPos = getNextPosition(currentPlayerPos, dir);
            if (isSafeDodgePosition(nodesToAvoid, nextPos, nearestEnemy)) {
                return dir;
            }
        }
        return null;
    }

    private boolean isSafeDodgePosition(List<Node> nodesToAvoid, Node nextPos, Player nearestEnemy) {
        if (!PathUtils.checkInsideSafeArea(nextPos, hero.getGameMap().getSafeZone(), hero.getGameMap().getMapSize()))
            return false;
        if (nodesToAvoid.contains(nextPos)) return false;

        double newDistance = PathUtils.distance(nextPos, nearestEnemy);
        double currentDistance = PathUtils.distance(hero.getGameMap().getCurrentPlayer(), nearestEnemy);

        return newDistance > currentDistance;
    }

    private List<Node> getNodesToAvoid(GameMap gameMap) {
        List<Node> nodes = new ArrayList<>();
        nodes.addAll(gameMap.getObstaclesByTag("CAN_GO_THROUGH"));
        nodes.addAll(gameMap.getOtherPlayerInfo());
        return nodes;
    }

    private Player getNearestPlayer(GameMap gameMap, Player player) {
        List<Player> otherPlayers = gameMap.getOtherPlayerInfo();
        Player target = null;
        int minDistance = 99999;
        for (Player otherPlayer : otherPlayers) {
            if (otherPlayer.getHealth() > 0) {
                int distance = PathUtils.distance(player, otherPlayer);
                if (distance < minDistance) {
                    minDistance = distance;
                    target = otherPlayer;
                }
            }
        }
        return target;
    }

    private String findPathToGun(GameMap gameMap, List<Node> nodesToAvoid, Player player) {
        Weapon nearestGun = getNearestGun(gameMap, player);
        if (nearestGun == null) return null;
        return PathUtils.getShortestPath(gameMap, nodesToAvoid, player, nearestGun, false);
    }

    private Weapon getNearestGun(GameMap gameMap, Player player) {
        List<Weapon> guns = gameMap.getAllGun();
        Weapon nearestGun = null;
        double minDistance = Double.MAX_VALUE;

        for (Weapon gun : guns) {
            double distance = PathUtils.distance(player, gun);
            if (distance < minDistance) {
                minDistance = distance;
                nearestGun = gun;
            }
        }
        return nearestGun;
    }

    private String findPathToOtherPlayer(List<Node> nodesToAvoid, Player player, Player nearestPlayer) {
        return PathUtils.getShortestPath(hero.getGameMap(), nodesToAvoid, player, nearestPlayer, false);
    }

    private String getRandomDirection() {
        String[] directions = {"u", "d", "l", "r"};
        return directions[new Random().nextInt(directions.length)];
    }

    private Node getNextPosition(Node currentPos, String direction) {
        int newX = currentPos.x;
        int newY = currentPos.y;

        switch (direction) {
            case "u":
                newY--;
                break;
            case "d":
                newY++;
                break;
            case "l":
                newX--;
                break;
            case "r":
                newX++;
                break;
        }
        return new Node(newX, newY);
    }
}