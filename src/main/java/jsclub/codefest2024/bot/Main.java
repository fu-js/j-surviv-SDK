package jsclub.codefest2024.bot;

import io.socket.emitter.Emitter;
import jsclub.codefest2024.sdk.algorithm.Distance;
import jsclub.codefest2024.sdk.algorithm.ShortestPath;
import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.Hero;
import jsclub.codefest2024.sdk.model.obstacles.Obstacle;
import jsclub.codefest2024.sdk.model.players.Player;
import jsclub.codefest2024.sdk.model.weapon.Weapon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {
    private static final String SERVER_URL = "https://cf-server.jsclub.dev";
    private static final String GAME_ID = "198163";
    private static final String PLAYER_NAME = "top1";

    public static String randomMove() {
        String[] moves = {"u", "d", "l", "r"};
        return moves[(int) (Math.random() * moves.length)];
    }

    public static void main(String[] args) throws IOException {
        Hero hero = new Hero(GAME_ID, PLAYER_NAME);

        Emitter.Listener onMapUpdate = new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                try {
                    GameMap gameMap = hero.getGameMap();
                    gameMap.updateOnUpdateMap(args[0]);

                    Player player = gameMap.getCurrentPlayer();
                    List<Weapon> gunList = gameMap.getAllGun();
                    List<Obstacle> restricedList = gameMap.getListIndestructibleObstacles();
                    restricedList.addAll(gameMap.getListChests());
                    restricedList.addAll(gameMap.getListTraps());

                    Node currentNode = new Node(player.getX(), player.getY());
                    List<Node> gunNodes = new ArrayList<>();
                    List<Node> restrictedNodes = new ArrayList<>();
                    for (Weapon gun : gunList) {
                        gunNodes.add(new Node(gun.getX(), gun.getY()));
                    }

                    Node nearestGun = Distance.nearestNode(currentNode, gunNodes);

                    for (Obstacle o : restricedList) {
                        restrictedNodes.add(new Node(o.getX(), o.getY()));
                    }

                    boolean pickedUpGun = false;
                    if (currentNode.getX() == nearestGun.getX() && currentNode.getY() == nearestGun.getY() && pickedUpGun == false) {
                        pickedUpGun = true;
                    } else {
                        hero.move(ShortestPath.getShortestPath(gameMap, restrictedNodes, currentNode, nearestGun, false));
                    }

                    if(pickedUpGun){
                        hero.pickupItem();
                    }
                    boolean randMove = false;
                    boolean randShoot = true;
                    if (pickedUpGun == true) {
                        if (randShoot == true) {
                            hero.shoot(randomMove());
                            randShoot = false;
                            randMove = true;
                        }
                        if (randMove == true) {
                            hero.move(randomMove());
                            randMove = false;
                            randShoot = true;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        hero.setOnMapUpdate(onMapUpdate);
        hero.start(SERVER_URL);
    }
}