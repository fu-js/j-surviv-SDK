

import io.socket.emitter.Emitter;
import jsclub.codefest.sdk.Hero;
import jsclub.codefest.sdk.algorithm.PathUtils;
import jsclub.codefest.sdk.base.Node;
import jsclub.codefest.sdk.model.GameMap;
import jsclub.codefest.sdk.model.Inventory;
import jsclub.codefest.sdk.model.obstacles.Obstacle;
import jsclub.codefest.sdk.model.players.Player;
import jsclub.codefest.sdk.model.support_items.SupportItem;
import jsclub.codefest.sdk.model.weapon.Weapon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final String SERVER_URL = "https://cf25-server.jsclub.dev";
    private static final String GAME_ID = "192525";
    private static final String PLAYER_NAME = "lily";
    private static final String SECRET_KEY = "sk-QzpmiqwsQcGzZE9lPPEKqw:vJpcUbwUzYpSSj7QqrqPx4TrjPlYATfg-AnkYisTZN77J5hXRh3xs925DL6KdzgnKEjeWNcS6QAP6KsW-pHnxQ";
//    private static final String SECRET_KEY = "sk-HbwuDkLNRRya5SvoCKCVVQ:qNGGSN8d82o4m2tGJEWjpyJScDlnCHBn4Gg0K2Zdr9z1f76-9DTGQ5anZytbsN1mpfulkRffk01ukhhf3y7kEg";

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
    private int step = 0;


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
            Inventory heroInvent = hero.getInventory();

            if (player == null || player.getHealth() == 0) {
                System.out.println("Player is dead or data is not available.");
                return;
            }

            System.out.println("Inventory: "+hero.getInventory());


            // --- Check for and handle general stuck (no movement at all) ---
            handleStuckDetection(player); // This detects if the bot is literally not moving
            if (stuckCounter > Main.STUCK_LIMIT) {
                handleGeneralStuck(); // Renamed for clarity: this is for absolute non-movement
                return;
            }

            List<Node> nodesToAvoid = getNodesToAvoid(gameMap);
            Player nearestPlayer = getNearestPlayer(gameMap, player);


            // --- Original game logic follows if no stuck or oscillation issues ---
            if (heroInvent.getGun() == null) {
                if (heroInvent.getMelee().getId().compareToIgnoreCase("Hand") != 0 && PathUtils.distance(player, nearestPlayer) <= 4) {
                    handleCombatByMelee(nearestPlayer, nodesToAvoid, player);
                }
                handleSearchForGun(gameMap, player, nodesToAvoid);
            } else if (heroInvent.getMelee().getId().compareToIgnoreCase("Hand") == 0) {
                if (PathUtils.distance(player, nearestPlayer) <= 4) {
                    handleCombatByGun(nearestPlayer, nodesToAvoid, player);
                } else if (findPathToMelee(gameMap, nodesToAvoid, player) != null) {
                    handleSearchForMelee(gameMap, player, nodesToAvoid);
                } else {
                    handleFindNearestChest(gameMap, player, nodesToAvoid);
                }
            } else {
                if (step == 0) {
                    handleCombatByGun(nearestPlayer, nodesToAvoid, player);
                    step++;
                } else if (step == 1) {
                    handleCombatByMelee(nearestPlayer, nodesToAvoid, player);
                    step = 0;
                }
            }


        } catch (Exception e) {
            System.err.println("Critical error in call method: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleGeneralStuck() throws IOException {
        System.out.println("Bot is completely stuck (not moving)! Attempting random movement.");
        hero.move(getRandomDirection());
        stuckCounter = 0; // Reset stuck counter after attempting to move
    }

    private void handleStuckDetection(Player player) {
        if (isStuck(player)) {
            stuckCounter++;
            System.out.println("Detected stuck (no movement): " + stuckCounter + "/" + Main.STUCK_LIMIT);
        } else {
            stuckCounter = 0;
        }
        updateLastPosition(player); // This updates the `lastPosition` for the general stuck check
    }

    private boolean shouldDodge(Hero hero, Player nearestPlayer, Player player) {
        return hero.getInventory().getGun() == null &&
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

    private void handleSearchForHealing(GameMap gameMap, Player player, List<Node> nodesToAvoid) throws IOException {
        System.out.println("No Healing found. Searching for a healing.");
        String pathToHeal = findPathToHealing(gameMap, nodesToAvoid, player);

        if (pathToHeal != null) {
            if (pathToHeal.isEmpty()) {
                hero.pickupItem();
            } else {
                hero.move(pathToHeal);
            }
        } else {
            hero.move(getRandomDirection());
        }
    }

    private void handleSearchForSpecial(GameMap gameMap, Player player, List<Node> nodesToAvoid) throws IOException {
        System.out.println("No special found. Searching for a special.");
        String pathToSpecial = findPathToSpecial(gameMap, nodesToAvoid, player);

        if (pathToSpecial != null) {
            if (pathToSpecial.isEmpty()) {
                hero.pickupItem();
            } else {
                hero.move(pathToSpecial);
            }
        } else {
            hero.move(getRandomDirection());
        }
    }

    private void handleSearchForMelee(GameMap gameMap, Player player, List<Node> nodesToAvoid) throws IOException {
        System.out.println("No melee found. Searching for a melee.");
        String pathToMelee = findPathToMelee(gameMap, nodesToAvoid, player);

        if (pathToMelee != null) {
            if (pathToMelee.isEmpty()) {
                hero.pickupItem();
            } else {
                hero.move(pathToMelee);
            }
        } else {
            hero.move(getRandomDirection());
        }
    }

    private void handleSearchForThrowable(GameMap gameMap, Player player, List<Node> nodesToAvoid) throws IOException {
        System.out.println("No throwable found. Searching for a throwable.");
        String pathToThrowable = findPathToThrowable(gameMap, nodesToAvoid, player);

        if (pathToThrowable != null) {
            if (pathToThrowable.isEmpty()) {
                hero.pickupItem();
            } else {
                hero.move(pathToThrowable);
            }
        } else {
            hero.move(getRandomDirection());
        }
    }

    private String checkString (String path, int range) {
        if (path == null || path.length() > range) {
            return null;
        }
        String firstLetter = path.substring(0,1);
        for (int i = 0; i < path.length(); i++) {
            if(firstLetter.compareToIgnoreCase(path.charAt(i) + "") != 0) {
                return null;
            }
        }
        return firstLetter;
    }

    private void handleCombatByGun(Player nearestPlayer, List<Node> nodesToAvoid, Player player) throws IOException {
        if (nearestPlayer == null) {
            hero.move(getRandomDirection());
            return;
        }

        String pathToEnemy = findPathToOtherPlayer(nodesToAvoid, player, nearestPlayer);
        String checkString = checkString(pathToEnemy, hero.getInventory().getGun().getRange()[1]);
        if (checkString != null) {
            System.out.println("Enemy in range. Shooting!");
            hero.shoot(checkString);
        } else if (pathToEnemy != null) {
            System.out.println("Moving closer to enemy: " + pathToEnemy);
            hero.move(pathToEnemy);
        } else {
            hero.move(getRandomDirection());
        }
    }

    private void handleCombatByMelee(Player nearestPlayer, List<Node> nodesToAvoid, Player player) throws IOException {
        if (nearestPlayer == null) {
            hero.attack(getRandomDirection());
            return;
        }

        String pathToEnemy = findPathToOtherPlayer(nodesToAvoid, player, nearestPlayer);
        String checkString = checkString(pathToEnemy, 1);
        if (checkString != null) {
            System.out.println("Enemy in range. Attacking!");
            hero.attack(checkString);
        } else if (pathToEnemy != null) {
            System.out.println("Moving closer to enemy: " + pathToEnemy);
            hero.move(pathToEnemy);
        } else {
            hero.attack(getRandomDirection());
        }
    }
    private void handleCombatByThrowable(Player nearestPlayer, List<Node> nodesToAvoid, Player player) throws IOException {
        if (hero.getInventory().getThrowable()!=null){
            if (nearestPlayer == null) {
                hero.throwItem(getRandomDirection());
                return;
            }

            String pathToEnemy = findPathToOtherPlayer(nodesToAvoid, player, nearestPlayer);
            String checkString = checkString(pathToEnemy, pathToEnemy.length());
            if (checkString != null) {
                System.out.println("Enemy in range. Throw!");
                hero.throwItem(checkString);
            } else if (pathToEnemy != null) {
                System.out.println("Moving closer to enemy: " + pathToEnemy);
                hero.move(pathToEnemy);
            } else {
                hero.throwItem(getRandomDirection());
            }
        } else {
            return;
        }
    }
    private void handleCombatBySpecial(Player nearestPlayer, List<Node> nodesToAvoid, Player player) throws IOException {
        Weapon special = hero.getInventory().getSpecial();
        if (special!=null){
            if (nearestPlayer == null) {
                hero.useSpecial(getRandomDirection());
                return;
            }

            String pathToEnemy = findPathToOtherPlayer(nodesToAvoid, player, nearestPlayer);
            String checkString = checkString(pathToEnemy, special.getRange()[1]);
            if (checkString != null) {
                System.out.println("Enemy in range. Special!");
                hero.useSpecial(checkString);
            } else if (pathToEnemy != null) {
                System.out.println("Moving closer to enemy: " + pathToEnemy);
                hero.move(pathToEnemy);
            } else {
                hero.useSpecial(getRandomDirection());
            }
        } else {
            return;
        }
    }


    private String findPathToChest(GameMap gameMap, List<Node> nodesToAvoid, Player player) {
        Obstacle nearestChest = getNearestChest(gameMap, player);
        if (nearestChest == null) return null;
        return PathUtils.getShortestPath(gameMap, nodesToAvoid, player, nearestChest, false);
    }
    private String findPathToMelee(GameMap gameMap, List<Node> nodesToAvoid, Player player) {
        Weapon nearestMelee = getNearestMelee(gameMap, player);
        if (nearestMelee == null) return null;
        return PathUtils.getShortestPath(gameMap, nodesToAvoid, player, nearestMelee, false);
    }

    private String findPathToThrowable(GameMap gameMap, List<Node> nodesToAvoid, Player player) {
        Weapon nearestThrow = getNearestThrow(gameMap, player);
        if (nearestThrow == null) return null;
        return PathUtils.getShortestPath(gameMap, nodesToAvoid, player, nearestThrow, false);
    }


    private void handleFindNearestChest(GameMap gameMap, Player player, List<Node> nodesToAvoid) throws IOException {
        System.out.println("No chest found. Searching for a chest.");
        String pathToChest = findPathToChest(gameMap, nodesToAvoid, player);

        if (pathToChest != null) {
            if (pathToChest.length() == 1) {
                hero.attack(pathToChest);
            } else {
                hero.move(pathToChest);
            }
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
        List<Node> nodes = new ArrayList<>(gameMap.getListIndestructibles());

        nodes.removeAll(gameMap.getObstaclesByTag("CAN_GO_THROUGH"));
        nodes.addAll(gameMap.getObstaclesByTag("TRAP"));
        nodes.addAll(gameMap.getOtherPlayerInfo());
        nodes.addAll(gameMap.getObstaclesByTag("DESTRUCTIBLE"));
        return nodes;
    }

    private Player getNearestPlayer(GameMap gameMap, Player player) {
        List<Player> otherPlayers = gameMap.getOtherPlayerInfo();
        Player target = null;
        int minDistance = 99999;
        for (Player otherPlayer : otherPlayers) {
            if (otherPlayer.getHealth() > 0) {
                int distance = PathUtils.distance(player, otherPlayer);
                if (distance < minDistance && PathUtils.checkInsideSafeArea(otherPlayer, gameMap.getSafeZone(), gameMap.getMapSize())) {
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

    private String findPathToHealing(GameMap gameMap, List<Node> nodesToAvoid, Player player) {
        SupportItem nearestHeal = getNearestHealing(gameMap, player);
        if (nearestHeal == null) return null;
        return PathUtils.getShortestPath(gameMap, nodesToAvoid, player, nearestHeal, false);
    }

    private SupportItem getNearestHealing(GameMap gameMap, Player player) {
        List<SupportItem> heals = gameMap.getListSupportItems();
        SupportItem nearestHeal = null;
        double minDistance = Double.MAX_VALUE;

        for (SupportItem heal : heals) {
            double distance = PathUtils.distance(player, heal);
            if (distance < minDistance
//                    && heal.getHealingHP() > 0
            ) {
                minDistance = distance;
                nearestHeal = heal;
            }
        }
        return nearestHeal;
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
    private String findPathToSpecial(GameMap gameMap, List<Node> nodesToAvoid, Player player) {
        Weapon nearestSpecial = getNearestSpecial(gameMap, player);
        if (nearestSpecial == null) return null;
        return PathUtils.getShortestPath(gameMap, nodesToAvoid, player, nearestSpecial, false);
    }

    private Weapon getNearestSpecial(GameMap gameMap, Player player) {
        List<Weapon> specials = gameMap.getAllSpecial();
        Weapon nearestSpecial = null;
        double minDistance = Double.MAX_VALUE;

        for (Weapon special : specials) {
            double distance = PathUtils.distance(player, special);
            if (distance < minDistance) {
                minDistance = distance;
                nearestSpecial = special;
            }
        }
        return nearestSpecial;
    }

    private Obstacle getNearestChest(GameMap gameMap, Player player) {
        List<Obstacle> chests = gameMap.getObstaclesByTag("DESTRUCTIBLE");
        Obstacle nearestChest = null;
        double minDistance = Double.MAX_VALUE;

        for (Obstacle chest : chests) {
            double distance = PathUtils.distance(player, chest);
            if(!PathUtils.checkInsideSafeArea(chest, gameMap.getSafeZone(), gameMap.getMapSize())) {
                continue;
            }
            if (distance < minDistance) {
                minDistance = distance;
                nearestChest = chest;
            }
        }
        return nearestChest;
    }

    private Weapon getNearestMelee(GameMap gameMap, Player player) {
        List<Weapon> melees = gameMap.getAllMelee();
        Weapon nearestMelee = null;
        double minDistance = Double.MAX_VALUE;

        for (Weapon melee : melees) {
            double distance = PathUtils.distance(player, melee);
            if (distance < minDistance) {
                minDistance = distance;
                nearestMelee = melee;
            }
        }
        return nearestMelee;
    }

    private Weapon getNearestThrow(GameMap gameMap, Player player) {
        List<Weapon> throwables = gameMap.getAllThrowable();
        Weapon nearestThrow = null;
        double minDistance = Double.MAX_VALUE;

        for (Weapon throwable : throwables) {
            double distance = PathUtils.distance(player, throwable);
            if (distance < minDistance) {
                minDistance = distance;
                nearestThrow = throwable;
            }
        }
        return nearestThrow;
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