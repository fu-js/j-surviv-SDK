package jsclub.codefest2024.bot;

import io.socket.emitter.Emitter;
import jsclub.codefest2024.sdk.algorithm.ShortestPath;
import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.model.ElementType;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.Hero;
import jsclub.codefest2024.sdk.model.players.Player;
import jsclub.codefest2024.sdk.model.weapon.Weapon;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final String SERVER_URL = "https://cf-server.jsclub.dev";
    private static final String GAME_ID = "101300";
    private static final String PLAYER_NAME = "ptd";


    static boolean isRunToWeapon = false;
    static boolean isRunToPLayer = false;
    static String p = "urrl";

    private static long lastCallTime = 0;  // External variable to track time across calls

    public static String randomMove() {
        String[] moves = {"u", "d", "l", "r"};
        return moves[(int) (Math.random() * moves.length)];
    }

    public static boolean haveWeapon(Hero hero) {
        if (hero.getInventory().getGun() != null || hero.getInventory().getMelee().getId() != "HAND" || hero.getInventory().getListThrowable().size() != 0) {
            return true;
        }
        return false;

    }

    public static double distance(Player player1, Player player2) {
        return Math.sqrt(
                (player1.x - player2.x) * (player1.x - player2.x)
                        + (player1.y - player2.y) * (player1.y - player2.y)
        );
    }

    public static void main(String[] args) throws IOException {
        Hero hero = new Hero(GAME_ID, PLAYER_NAME);
        Emitter.Listener onMapUpdate = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                long currentTime = System.currentTimeMillis();
                if (lastCallTime != 0) {
                    long timeDifference = currentTime - lastCallTime;
                }
                lastCallTime = currentTime;  // Update the last call time

//                try {
//                    GameMap gameMap = hero.getGameMap();
//                    gameMap.updateOnUpdateMap(args[0]);
//
//                    Player player = gameMap.getCurrentPlayer();
//                    Weapon target = null;
//                    double distance = 10000000;
//                    for (Weapon weapon : gameMap.getListWeapons()) {
//                        //
//                        // System.out.println(weapon.x + " " + weapon.y);
//                        if (distance > Math.sqrt(
//                                (player.x - weapon.x) * (player.x - weapon.x)
//                                        + (player.y - weapon.y) * (player.y - weapon.y)
//                        )) {
//                            distance = (player.x - weapon.x) * (player.x - weapon.x)
//                                    + (player.y - weapon.y) * (player.y - weapon.y);
//                            target = weapon;
//                        }
//                    }
//                    if (!haveWeapon(hero)) {
//                        if (!isRunToWeapon) {
//                            if (target != null) {
//                                if (target.getType() == ElementType.MELEE) {
//                                    if (hero.getInventory().getMelee().getId() == "HAND") {
//                                        String path = ShortestPath.getShortestPath(
//                                                gameMap,
//                                                List.of(),
//                                                player,
//                                                target,
//                                                false
//                                        );
//                                        hero.move(path);
//                                        isRunToWeapon = true;
//                                    }
//                                } else if (target.getType() == ElementType.GUN) {
//                                    if (hero.getInventory().getGun() == null) {
//                                        String path = ShortestPath.getShortestPath(
//                                                gameMap,
//                                                List.of(),
//                                                player,
//                                                target,
//                                                true
//                                        );
//                                        hero.move(path);
//                                        isRunToWeapon = true;
//                                    }
//                                } else {
//                                    String path = ShortestPath.getShortestPath(
//                                            gameMap,
//                                            List.of(),
//                                            player,
//                                            target,
//                                            true
//                                    );
//                                    hero.move(path);
//                                    isRunToWeapon = true;
//                                }
//                            }
//                        } else {
//                            try {
////                                System.out.println("MELEE: " + hero.getInventory().getMelee());
////                                System.out.println("GUN: " + hero.getInventory().getGun());
////                                System.out.println("THROWABLE: " + hero.getInventory().getListThrowable());
//                                List<Weapon> weapons = gameMap.getListWeapons();
//                                for (Weapon weapon : weapons) {
//                                    if (weapon.x == player.x && weapon.y == player.y) {
//                                        if (weapon.getType() == ElementType.MELEE) {
////                                            hero.getInventory().setMelee(weapon);
//                                            System.out.println("PICKUP MELEE");
//                                        }
//                                        if (weapon.getType() == ElementType.GUN) {
////                                            hero.getInventory().setGun(weapon);
//                                            System.out.println("PICKUP GUN");
//                                        }
//                                        if (weapon.getType() == ElementType.THROWABLE) {
////                                            hero.getInventory().getListThrowable().add(weapon);
//                                            System.out.println("PICKUP THROWABLE");
//                                        }
//                                        hero.pickupItem();
//                                        isRunToWeapon = false;
//                                    }
//                                }
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    } else {
//                        String path = ShortestPath.getShortestPath(
//                                gameMap,
//                                List.of(),
//                                player,
//                                gameMap.getOtherPlayerInfo().get(0),
//                                true
//                        );
//                        if (!isRunToPLayer) {
//                            hero.move(path);
//                        } else {
//                            System.out.println(distance(player, gameMap.getOtherPlayerInfo().get(0)));
//                            System.out.println(hero.getInventory().getGun().getRange());
//                            if (distance(player, gameMap.getOtherPlayerInfo().get(0)) < hero.getInventory().getGun().getRange()) {
//                                hero.shoot(randomMove());
//                                isRunToPLayer = true;
//                            }
//                        }
//                    }
//
////                    if (!gameMap.getCurrentPlayer().getIsAlive()) {
////                        isRunToPLayer = false;
////                        isRunToWeapon = false;
////                        hero.getInventory().setGun(null);
////                        hero.getInventory().setMelee(new Weapon("HAND", ElementType.MELEE, 0, 0, 0, 0));
////                        hero.getInventory().getListThrowable().clear();
////                    }
//
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//
//                }

                GameMap gameMap = hero.getGameMap();
                gameMap.updateOnUpdateMap(args[0]);
                Player player = gameMap.getCurrentPlayer();
                List<Weapon> weapon = gameMap.getListWeapons();
                Weapon target = null;
                int length = Integer.MAX_VALUE;
                for (Weapon w : weapon) {
                    String p = ShortestPath.getShortestPath(
                            gameMap,
                            List.of(),
                            player,
                            w,
                            true
                    );
                    if (p.length() < length) {
                        length = p.length();
                        target = w;
                    }
                }
                String path = ShortestPath.getShortestPath(
                        gameMap,
                        List.of(),
                        player,
                        target,
                        true
                );
                try {
                    hero.move(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        };
        hero.setOnMapUpdate(onMapUpdate);
        hero.start(SERVER_URL);
    }
}
