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
    private static final String GAME_ID = "113706";
    private static final String PLAYER_NAME = "lily";
    private static final String SECRET_KEY = "sk-_1cbwgEAQ_-1nRK0TsThqw:sfTqg9SnxrLE38umH71MTWzl0emAQWli6-aO4UaxWOQjrBxRhQjh--hi3yqGRP93pwMGn9Muw6WLWHLTRAxOpQ";

    public static final int STUCK_LIMIT = 4;
    public static final int DODGE_RANGE = 3;

    public static void main(String[] args) throws IOException {
        Hero hero = new Hero(GAME_ID, PLAYER_NAME, SECRET_KEY);

        Emitter.Listener onMapUpdate = new Emitter.Listener() {
            private int stuckCounter = 0;
            private Node lastPosition = new Node(-1, -1); // Vị trí ở lần chạy trước
            private static final int STUCK_LIMIT = 4; // Ngưỡng nhận diện bị kẹt

            @Override
            public void call(Object... args) {
                try {
                    // --- BƯỚC 0: KIỂM TRA AN TOÀN VÀ CẬP NHẬT TRẠNG THÁI ---
                    if (args == null || args.length == 0)
                        return;

                    GameMap gameMap = hero.getGameMap();
                    gameMap.updateOnUpdateMap(args[0]);
                    Player player = gameMap.getCurrentPlayer();

                    List<Player> otherPlayers = gameMap.getOtherPlayerInfo();
                    Player nearestPlayer = getNearestPlayer(gameMap, player);

                    // restricted list
                    List<Obstacle> traps = gameMap.getObstaclesByTag("TRAP");
                    List<Obstacle> initThings = gameMap.getListObstacleInit();
                    List<Obstacle> canGoThroughs = gameMap.getObstaclesByTag("CAN_GO_THROUGH");

                    List<Node> restrictedNodes = new ArrayList<>(initThings);
                    restrictedNodes.removeAll(canGoThroughs);
                    restrictedNodes.addAll(traps);

                    // restricted with other players
                    List<Node> restrictedNodeWithOtherPlayers = new ArrayList<>(restrictedNodes);
                    restrictedNodeWithOtherPlayers.addAll(otherPlayers);

                    ///////////// ----algorithm-----////////////

                    if (player == null || player.getHealth() == 0) {
                        System.out.println("Nhân vật đã chết hoặc chưa có dữ liệu.");
                        return;
                    }

                    // --- BƯỚC 1: XỬ LÝ CHỐNG KẸT (ANTI-STUCK) ---
                    if (isStuck(player)) {
                        stuckCounter++;
                        System.out.println("Phát hiện có dấu hiệu bị kẹt: " + stuckCounter + "/" + STUCK_LIMIT);
                    } else {
                        stuckCounter = 0; // Reset bộ đếm nếu có di chuyển
                    }
                    updateLastPosition(player);

                    if (stuckCounter > STUCK_LIMIT) {
                        System.out.println("Bị kẹt! Thử di chuyển ngẫu nhiên để thoát.");
                        hero.move(getRandomDirection());
                        stuckCounter = 0; // Reset sau khi đã xử lý
                        return;
                    }

                    // --- BƯỚC 2: XÂY DỰNG CÂY QUYẾT ĐỊNH THÔNG MINH ---

                    // LUỒNG 1: ƯU TIÊN HÀNG ĐẦU - KIẾM SÚNG NẾU CHƯA CÓ HOẶC HẾT ĐẠN
                    if (player.getInventory().getGun() == null) {
                        System.out.println("Chưa có súng. Đi tìm súng.");

                        String pathToGun = findPathToGun(gameMap, restrictedNodes, player);
                        if (pathToGun != null) {
                            if (pathToGun.isEmpty()) {
                                hero.pickupItem();
                            } else {
                                hero.move(pathToGun);
                            }
                        } else {
                            // Không tìm thấy súng, tạm thời đi tấn công bằng vũ khí cận chiến
                            System.out.println("Không có súng trên bản đồ, chuyển sang cận chiến.");
                            // handleMeleeAttack(hero, gameMap, restrictedNodes, player, nearestPlayer);
                            String path = findPathToOtherPlayer(gameMap, restrictedNodes, player, nearestPlayer);
                            if (path.length() <= 1) {
                                hero.attack(path);
                            }

                        }
                        return;
                    } else {
                        String pathToEnemy = findPathToOtherPlayer(gameMap, restrictedNodes, player, nearestPlayer);

                        if (pathToEnemy != null) {
                            if (canAttackByThrowable(pathToEnemy, player) != null) {
                                hero.throwItem(canAttackByThrowable(pathToEnemy, player),
                                        player.getInventory().getThrowable().getRange());
                            }
                            if (canAttackByMelee(pathToEnemy, player) != null) {
                                hero.attack(canAttackByMelee(pathToEnemy, player));
                            }
                            if (canAttackBySpecial(pathToEnemy, player) != null) {
                                hero.attack(canAttackBySpecial(pathToEnemy, player));
                            }
                            if (canAttackByGun(pathToEnemy, player) != null) {
                                hero.shoot(canAttackByGun(pathToEnemy, player));
                            }
                            // Nếu không tấn công được, di chuyển lại gần
                            else {
                                System.out.println("Di chuyển đến gần kẻ địch hơn: " + pathToEnemy);
                                hero.move(pathToEnemy);
                            }
                        } else {
                            System.out.println("Không tìm thấy kẻ địch, di chuyển ngẫu nhiên.");
                            hero.move(getRandomDirection());
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Lỗi nghiêm trọng trong hàm call: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // --- CÁC HÀM HỖ TRỢ CHO VIỆC CHỐNG KẸT ---
            private boolean isStuck(Player player) {
                return player.x == lastPosition.x && player.y == lastPosition.y;
            }

            private void updateLastPosition(Player player) {
                lastPosition.setPosition(player.x, player.y);
            }
        };

        hero.setOnMapUpdate(onMapUpdate);
        hero.start(SERVER_URL);
    }

    // ============================================================================================
    // CÁC HÀM HỖ TRỢ (HELPER FUNCTIONS)
    // ============================================================================================

    private static String findPathToOtherPlayer(GameMap gameMap, List<Node> nodes, Player player,
            Player nearestPlayer) {
        if (nearestPlayer == null)
            return null;
        List<Node> tempNodes = new ArrayList<>(nodes);
        tempNodes.remove(nearestPlayer);
        return PathUtils.getShortestPath(gameMap, tempNodes, player, nearestPlayer, false);
    }

    private static String findPathToGun(GameMap gameMap, List<Node> nodes, Player player) {
        System.out.println("path to gun: " + gameMap.getAllGun().isEmpty());
        if (gameMap.getAllGun().isEmpty())
            return null;
        Weapon nearestGun = getNearestGun(gameMap, player);
        if (nearestGun == null)
            return null;
        return PathUtils.getShortestPath(gameMap, nodes, player, nearestGun, false);
    }

    private static Player getNearestPlayer(GameMap gameMap, Player player) {
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
        if (nearestGun == null)
            return null;
        return PathUtils.getShortestPath(gameMap, nodesToAvoid, player, nearestGun, false);
    }

    private static Weapon getNearestGun(GameMap gameMap, Player player) {
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

    private static String findPathToOtherPlayer(List<Node> nodesToAvoid, Player player, Player nearestPlayer) {
        return PathUtils.getShortestPath(hero.getGameMap(), nodesToAvoid, player, nearestPlayer, false);
    }

    private static String getRandomDirection() {
        String[] directions = { "u", "d", "l", "r" };
        return directions[new Random().nextInt(directions.length)];
    }

    private static Node getNextPosition(Node currentPos, String direction) {
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

    private static String canAttackByThrowable(String path, Player player) {
        if (!isHaveThrowable(player))
            return null;
        return checkString(path, player.getInventory().getThrowable().getRange());
    }

    private static String canAttackBySpecial(String path, Player player) {
        if (!isHaveSpecial(player))
            return null;
        return checkString(path, player.getInventory().getSpecial().getRange());
    }

    private static String canAttackByGun(String path, Player player) {
        if (!isHaveGun(player))
            return null;
        return checkString(path, player.getInventory().getGun().getRange());
    }

    private static String canAttackByMelee(String path, Player player) {
        if (!isHaveMelee(player))
            return null;
        return checkString(path, player.getInventory().getMelee().getRange());
    }

    private static boolean isHaveSpecial(Player player) {
        return player.getInventory().getSpecial() != null;
    }

    private static boolean isHaveThrowable(Player player) {
        return player.getInventory().getThrowable() != null;
    }

    private static boolean isHaveGun(Player player) {
        return player.getInventory().getGun() != null;
    }

    private static boolean isHaveMelee(Player player) {
        return player.getInventory().getMelee() != null &&
                !player.getInventory().getMelee().getId().equalsIgnoreCase("HAND");
    }
}
