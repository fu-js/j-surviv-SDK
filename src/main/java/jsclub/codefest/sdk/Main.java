package jsclub.codefest.sdk;

import io.socket.emitter.Emitter;
import jsclub.codefest.sdk.algorithm.PathUtils;
import jsclub.codefest.sdk.base.Node;
import jsclub.codefest.sdk.model.GameMap;
import jsclub.codefest.sdk.model.obstacles.Obstacle;
import jsclub.codefest.sdk.model.players.Player;
import jsclub.codefest.sdk.model.weapon.Weapon;

import java.io.IOException;
import java.util.*;

public class Main {
    private static final String SERVER_URL = "https://cf25-server-staging.jsclub.dev";
    private static final String GAME_ID = "124472";
    private static final String PLAYER_NAME = "smartbot";
    private static final String SECRET_KEY = "sk-qg_PU4LVSjayxzhWKLhYoA:ugsmwXKvksR3VqxK7_QeXMmew3zFq6Yghiicc_4uJX1StuTROImu9kguOQKj0TeHa4wD-5MCu5-PDUwLjLTYTg"; // thay bằng key thật

    public static void main(String[] args) throws IOException {
        Hero hero = new Hero(GAME_ID, PLAYER_NAME, SECRET_KEY);

        Emitter.Listener onMapUpdate = new Emitter.Listener() {
            private Node lastPosition = new Node(-1, -1);
            private int stuckCount = 0;

            @Override
            public void call(Object... args) {
                try {
                    if (args == null || args.length == 0) return;
                    GameMap gameMap = hero.getGameMap();
                    gameMap.updateOnUpdateMap(args[0]);
                    Player player = gameMap.getCurrentPlayer();


                    if (player == null || player.getHealth() == 0) return;


                    System.out.println("Current inventory: " + player.getInventory().getGun().getId());

                    // Chống kẹt
                    if (player.x == lastPosition.x && player.y == lastPosition.y) {
                        stuckCount++;
                        if (stuckCount > 3) {
                            hero.move(getRandomDirection());
                            stuckCount = 0;
                            return;
                        }
                    } else {
                        stuckCount = 0;
                    }
                    lastPosition.setPosition(player.x, player.y);

                    // Hồi máu
                    if (player.getHealth() < 50 && !player.getInventory().getListHealingItem().isEmpty()) {
                        hero.useItem(player.getInventory().getListHealingItem().get(0).getId());
                        return;
                    }

                    // Ưu tiên nhặt giáp
//                    if (player.getInventory().getArmor() == null || player.getInventory().getHelmet() == null) {
//                        Node eqNode = gameMap.getNearestArmorOrHelmet(player.x, player.y);
//                        if (eqNode != null) {
//                            String path = PathUtils.getShortestPath(gameMap, new ArrayList<>(), player, eqNode, false);
//                            if (path != null && !path.isEmpty()) {
//                                hero.move(path);
//                                return;
//                            } else {
//                                hero.pickupItem();
//                                return;
//                            }
//                        }
//                    }

                    // Tìm súng nếu chưa có
                    if (player.getInventory().getGun() == null) {
                        Weapon gun = getNearestGun(gameMap, player);
                        if (gun != null) {
                            String path = PathUtils.getShortestPath(gameMap, new ArrayList<>(), player, gun, false);
                            if (path != null && !path.isEmpty()) {
                                hero.move(path);
                                return;
                            } else {
                                hero.pickupItem();
                                return;
                            }
                        }
                    }

                    // Tìm và tấn công địch
                    Player enemy = getNearestEnemy(gameMap, player);
                    if (enemy != null) {
                        String path = PathUtils.getShortestPath(gameMap, new ArrayList<>(), player, enemy, false);
                        if (canAttack(path, player)) {
                            hero.shoot(path.substring(0, 1));
                        } else {
                            hero.move(path);
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Lỗi bot: " + e.getMessage());
                }
            }

            private String getRandomDirection() {
                String[] directions = {"u", "d", "l", "r"};
                return directions[new Random().nextInt(directions.length)];
            }

            private boolean canAttack(String path, Player p) {
                return path != null && path.length() <= p.getInventory().getGun().getRange()
                        && path.chars().allMatch(c -> c == path.charAt(0));
            }

            private Player getNearestEnemy(GameMap map, Player me) {
                double minDist = Double.MAX_VALUE;
                Player nearest = null;
                for (Player p : map.getOtherPlayerInfo()) {
                    if (p.getHealth() > 0) {
                        double dist = Math.pow(me.x - p.x, 2) + Math.pow(me.y - p.y, 2);
                        if (dist < minDist) {
                            minDist = dist;
                            nearest = p;
                        }
                    }
                }
                return nearest;
            }

            private Weapon getNearestGun(GameMap map, Player me) {
                double minDist = Double.MAX_VALUE;
                Weapon gun = null;
                for (Weapon w : map.getAllGun()) {
                    double dist = Math.pow(me.x - w.x, 2) + Math.pow(me.y - w.y, 2);
                    if (dist < minDist) {
                        minDist = dist;
                        gun = w;
                    }
                }
                return gun;
            }
        };

        hero.setOnMapUpdate(onMapUpdate);
        hero.start(SERVER_URL);
    }
}
