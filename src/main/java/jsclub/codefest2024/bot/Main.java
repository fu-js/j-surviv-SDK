package jsclub.codefest2024.bot;

import io.socket.emitter.Emitter;
import jsclub.codefest2024.sdk.algorithm.MovePath;
import jsclub.codefest2024.sdk.algorithm.ShortestPath;
import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.Hero;
import jsclub.codefest2024.sdk.model.obstacles.Obstacle;
import jsclub.codefest2024.sdk.model.players.Player;
import jsclub.codefest2024.sdk.model.weapon.Weapon;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String SERVER_URL = "https://cf-server.jsclub.dev";
    private static final String GAME_ID = "148660";
    private static final String PLAYER_NAME = "ptd";


    static String p = "urrl";

    private static long lastCallTime = 0;  // External variable to track time across calls

    public static String randomMove() {
        String[] moves = {"u", "d", "l", "r"};
        return moves[(int) (Math.random() * moves.length)];
    }

    public static void main(String[] args) throws IOException {
        Hero hero = new Hero(GAME_ID, PLAYER_NAME);
        GameMap gameMap = hero.getGameMap();
        Emitter.Listener onMapUpdate = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                long currentTime = System.currentTimeMillis();
                if (lastCallTime != 0) {
                    long timeDifference = currentTime - lastCallTime;
//                    System.out.println("Time between calls: " + timeDifference + " ms");
                }
                lastCallTime = currentTime;  // Update the last call time
                try {
                    gameMap.updateOnUpdateMap(args[0]);

                    Player player = gameMap.getCurrentPlayer();
                    Weapon target = null;
                    double distance = 10000000;
                    for (Weapon weapon : gameMap.getAllGun()) {
                        if (distance > Math.sqrt(
                                (player.x - weapon.x) * (player.x - weapon.x)
                                        + (player.y - weapon.y) * (player.y - weapon.y)
                        )) {
                            distance = (player.x - weapon.x) * (player.x - weapon.x)
                                    + (player.y - weapon.y) * (player.y - weapon.y);
                            target = weapon;
                        }
                    }
                    System.out.println("GUN: " + hero.getInventory().getGun());
                    List<Weapon> weapons = gameMap.getAllGun();
                    for (Weapon weapon : weapons) {
                        if (weapon.x == player.x && weapon.y == player.y) {
                            if (hero.getInventory().getGun() != null) {
                                hero.revokeItem(hero.getInventory().getGun().getId());
                                System.out.println("DROP");
                            } else {
                                hero.pickupItem();
                                System.out.println("PICKUP");
                            }
                        }
                    }

//                    List<Player> players = gameMap.getOtherPlayerInfo();
//                    hero.move(ShortestPath.getShortestPath(
//                            gameMap,
//                            List.of(),
//                            player,
//                            new Node(players.get(0).x, players.get(0).y),
//                            false
//                    ));
//                    hero.move(randomMove());

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        hero.setOnMapUpdate(onMapUpdate);
        hero.start(SERVER_URL);
    }
}
