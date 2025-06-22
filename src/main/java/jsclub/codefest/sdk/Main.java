package jsclub.codefest.sdk;


import com.google.gson.internal.bind.util.ISO8601Utils;
import io.socket.emitter.Emitter;
import jsclub.codefest.sdk.algorithm.PathUtils;
import jsclub.codefest.sdk.base.Node;
import jsclub.codefest.sdk.factory.WeaponFactory;
import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.GameMap;
import jsclub.codefest.sdk.model.npcs.Ally;
import jsclub.codefest.sdk.model.npcs.Enemy;
import jsclub.codefest.sdk.model.obstacles.Obstacle;
import jsclub.codefest.sdk.model.obstacles.ObstacleTag;
import jsclub.codefest.sdk.model.players.Player;
import jsclub.codefest.sdk.model.weapon.Weapon;
import jsclub.codefest.sdk.socket.data.receive_data.Entity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




public class Main {
    private static final String SERVER_URL = "https://cf25-server-staging.jsclub.dev";
    //    private static final String SERVER_URL = "http://192.168.123.126:3000";
    private static final String GAME_ID = "143198";
    private static final String PLAYER_NAME = "phi";
    private static final String SECRET_KEY = "sk-eBxDjEN1RHOSiq5Dp1Y2UA:_ubE3ufCS9UO562iLFTNSqcK5vxAl93EWMvfFrvxyFrgcw5MVz3gd3I-8TONOIocQvuZx4mqtxyfBSV8xUpNNQ";
//    private static final String SECRET_KEY = "sk-bKmGDraDRVSV1opmx1gZ7Q:YYrurLaSNj8-FVHpMiYtfWn5quvLda1NevFOe6d41QL3JCKDl8WVy5xsi34fXdjWS3SRQIdGtCNBD8Hk-gkWHw";


    public static void main(String[] args) throws IOException {
        Hero hero = new Hero(GAME_ID, PLAYER_NAME, SECRET_KEY);

        Emitter.Listener onMapUpdate = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {


//              tring path = PathUtils.getShortestPath(gameMap,null,new Node(currenPlayer.getX(), currenPlayer.getY()),new Node(20,20),true);
                    System.out.println("Start a try");
                    GameMap gameMap = hero.getGameMap(); // map
                    //gameMap0.updateOnInitMap(args[0]);
                    gameMap.updateOnUpdateMap(args[0]);
                    //System.out.println("GAMEMAP INIT: " + gameMap0);
                    System.out.println("GAMEMAP UPDATE: " + gameMap);

                    List<Enemy> listEnemies = gameMap.getListEnemies();
                    List<Obstacle> listObstacles = gameMap.getListObstacles();
                    List<Obstacle> listCanGoThroughObstacles = new ArrayList<>();
                    List<Obstacle> listCannotGoThroughObstacles = gameMap.getListObstacles();
                    List<Element> listRestricted = new ArrayList<>();
                    List<Node> listRestrictedNode = new ArrayList<>();
                    setupAllList(gameMap, listEnemies, listObstacles, listCanGoThroughObstacles, listCannotGoThroughObstacles, listRestricted, listRestrictedNode);
                    Player player = gameMap.getCurrentPlayer();
                    getPosition(player);
                    System.out.println("THE LENGTH OF SAFEZONE: " + gameMap.getSafeZone());
                    if (player.getInventory().getGun() == null)
                    {
                        hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode,getNearestWeaponInSafezoneByID(gameMap, player, "SHOTGUN") ));
                        if (player.x == getNearestWeaponInSafezoneByID(gameMap, player, "SHOTGUN").x && player.y == getNearestWeaponInSafezoneByID(gameMap, player, "SHOTGUN").y) {
                            hero.pickupItem();
                        }
                    }
                    else {
                        if (getDistance(gameMap, player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.PLAYER)) == 1)
                            hero.shoot(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.PLAYER)));
                        else
                            hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.PLAYER)));
                    }

                }
                catch (IOException ex) {
                    System.err.println("Error" + ex);
                }






            }
        };
        hero.setOnMapUpdate(onMapUpdate);
        hero.start(SERVER_URL);
    }


    // Lấy vị trí hiện tại của Element
    public static void getPosition(Node node) {
        System.out.println("POSITION: " + node.x + "/" + node.y);
    }

    // Lấy Element gần nhất nằm trong Safezone bằng ElementType (Player, Ally)
    public static Element getNearestTargetInSafezoneByElementType(GameMap gameMap, Player player, ElementType elementType) {
        Element target = null;
        if (elementType.equals(ElementType.PLAYER)) {
            List<Player> listPlayer = gameMap.getOtherPlayerInfo();
            double minDistance = Double.MAX_VALUE;
            for (Player p : listPlayer) {
                double distance = getDistance(gameMap, player, p);
                if (distance < minDistance && PathUtils.checkInsideSafeArea(p, gameMap.getSafeZone(), gameMap.getMapSize())) {
                    target = p;
                    minDistance = distance;
                }
            }
        }

        if (elementType.equals(ElementType.ALLY)) {
            List<Ally> listAlly = gameMap.getListAllies();
            double minDistance = Double.MAX_VALUE;
            for (Ally a : listAlly) {
                double distance = getDistance(gameMap, player, a);
                if (distance < minDistance && PathUtils.checkInsideSafeArea(a, gameMap.getSafeZone(), gameMap.getMapSize())) {
                    target = a;
                    minDistance = distance;
                }
            }
        }
        return target;

    }

    //Lấy Weapon gần nhất nằm trong SafeZone bằng ID
    public static Weapon getNearestWeaponInSafezoneByID(GameMap gameMap, Player player, String ID) {
        Weapon target = null;
        Weapon gun = WeaponFactory.getWeaponById(ID);
        List<Weapon> listGun = gameMap.getAllGun();
        List <Weapon> listTarget = new ArrayList<>();
        for (Weapon g : listGun) {
            if (g.getId().compareTo("ID") == 0)
                listTarget.add(g);

        }
        double minDistance = Double.MAX_VALUE;
        for (Weapon g : listTarget) {
                double distance = getDistance(gameMap, player, g);
                if (distance < minDistance && PathUtils.checkInsideSafeArea(g, gameMap.getSafeZone(), gameMap.getMapSize())) {
                    target = g;
                    minDistance = distance;
                }
            }
        return target;

    }

    // Lấy String đường ngắn nhất đến Mục tiêu (Element) gần nhất
    public static String getPathToNearestTarget(GameMap gameMap, Player player, List<Node> listRestrictedNode, Element target) {
        String path = null;
        System.out.println("TARGET: " + target);
        System.out.println("GAMEMAP: " + gameMap);
        System.out.println("List Restricted Node: " + listRestrictedNode);
        System.out.println("Player: " + player);
        System.out.println("Target: " + target);

        System.out.println("List before remove: " + listRestrictedNode.size());


        listRestrictedNode.removeIf(n -> n.x == target.x && n.y == target.y);
        System.out.println("List after remove: " + listRestrictedNode.size());

        path = PathUtils.getShortestPath(gameMap, listRestrictedNode, player, target, true);
        System.out.println("PATH: " + path);
        return String.valueOf(path.charAt(0));

    }

    // Lấy Obstacle Chest gần nhất nằm trong Safezone
    public static Obstacle getNearestChestInSafezone(GameMap gameMap, Player player) {
        Obstacle target = null;

        List<Obstacle> listDestructibleObstacles = gameMap.getObstaclesByTag("DESTRUCTIBLE");
        double minDistance = Double.MAX_VALUE;
        for (Obstacle o : listDestructibleObstacles) {
            double distance = Math.pow(player.x - o.x, 2) + Math.pow(player.y - o.y, 2);
            if (distance < minDistance && PathUtils.checkInsideSafeArea(o, gameMap.getSafeZone(), gameMap.getMapSize())) {
                target = o;
                minDistance = distance;
            }
        }
        return target;
    }

    // Lấy khoảng cách từ target1 đến target2
    public static double getDistance(GameMap gameMap, Element target1, Element target2) {
        return Math.pow(target1.x - target2.x, 2) + Math.pow(target1.y - target2.y, 2);
    }


    //Setup các List
    public static void setupAllList(GameMap gameMap, List<Enemy> listEnemies, List<Obstacle> listObstacles, List<Obstacle> listCanGoThroughObstacles, List<Obstacle> listCannotGoThroughObstacles, List<Element> listRestricted, List<Node> listRestrictedNode) {
        System.out.println("LIST ENEMIES: " + listEnemies);
        System.out.println("LIST OBSTACLES: " + listObstacles);
        for (Obstacle o : listObstacles) {
            List<ObstacleTag> listTag = o.getTag();
            for (ObstacleTag t : listTag) {
                if (t == ObstacleTag.CAN_GO_THROUGH) {
                    listCanGoThroughObstacles.add(o);
                    listCannotGoThroughObstacles.remove(o);
                    break;
                }
            }
        }
        System.out.println("LIST CAN GO THROUGH OBSTACLES: " + listCanGoThroughObstacles);
        System.out.println("LIST CANNOT GO THROUGH OBSTACLES: " + listCannotGoThroughObstacles);
        for (Enemy e : listEnemies) {
            listRestricted.add(e);
            listRestrictedNode.add(e);
        }
        for (Obstacle o : listCannotGoThroughObstacles) {
            listRestricted.add(o);
            listRestrictedNode.add(o);
        }
        System.out.println("LIST RESTRICTED: " + listRestricted);
        System.out.println("LIST RESTRICTED NODE: " + listRestrictedNode);

    }

    public static void findAndDestroyChess(Hero hero, GameMap gameMap, Player player, List<Node> listRestrictedNode) {
        try {
            hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestChestInSafezone(gameMap, player)));
            if (getDistance(gameMap, player, getNearestChestInSafezone(gameMap, player)) == 1) {
                hero.attack(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestChestInSafezone(gameMap, player)));
            }
        } catch (IOException ex) {
            System.err.println("Error" + ex);
        }
    }
}





