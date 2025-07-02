package jsclub.codefest.sdk;




import io.socket.emitter.Emitter;
import jsclub.codefest.sdk.Hero;
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
import jsclub.codefest.sdk.model.support_items.SupportItem;
import jsclub.codefest.sdk.model.weapon.Weapon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String SERVER_URL = "https://cf25-server.jsclub.dev";
    //    private static final String SERVER_URL = "http://192.168.123.126:3000";
    private static final String GAME_ID = "152700";
    private static final String PLAYER_NAME = "anhtu";
    private static final String SECRET_KEY = "sk-MtStjZ5iS7m9Mzwt23HdTQ:KiNPBE-GTNAweB3V58R9qhe3FmNPJmqRGo7m2l5t3W9hgcYFiPyNPf6yxsAbNuh7JoeB7mivJWaAiMSm4Os4hQ";
    //    private static final String SECRET_KEY = "sk-bKmGDraDRVSV1opmx1gZ7Q:YYrurLaSNj8-FVHpMiYtfWn5quvLda1NevFOe6d41QL3JCKDl8WVy5xsi34fXdjWS3SRQIdGtCNBD8Hk-gkWHw";


    public static void main(String[] args) throws IOException {
        Hero hero = new Hero(GAME_ID, PLAYER_NAME, SECRET_KEY);
        final int[] i = {0};


        Emitter.Listener onMapUpdate = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    //              tring path = PathUtils.getShortestPath(gameMap,null,new Node(currenPlayer.getX(), currenPlayer.getY()),new Node(20,20),true);
                    System.out.println("Start a try");
                    System.out.println("i: " + i[0]);
                    GameMap gameMap = hero.getGameMap(); // map
                    gameMap.updateOnUpdateMap(args[0]);
                    //System.out.println("GAMEMAP UPDATE: " + gameMap);
                    List<Enemy> listEnemies = new ArrayList<>(gameMap.getListEnemies());
                    List<Obstacle> listIndestructibles = new ArrayList<>(gameMap.getListIndestructibles());
                    List<Obstacle> listObstacles = new ArrayList<>(gameMap.getListObstacles());
                    List<Obstacle> listChests = new ArrayList<>(gameMap.getListChests()) ;
                    List<Obstacle> listTraps = new ArrayList<>(gameMap.getListTraps());
                    List<Obstacle> listCanGoThroughObstacles = new ArrayList<>();
                    List<Obstacle> listCannotGoThroughObstacles = new ArrayList<>(gameMap.getListObstacles());
                    List<Element> listRestricted = new ArrayList<>();
                    List<Node> listRestrictedNode = new ArrayList<>();
                    setupAllList(listEnemies, listIndestructibles, listObstacles, listChests, listTraps, listCanGoThroughObstacles, listCannotGoThroughObstacles,  listRestricted, listRestrictedNode);
                    Player player = gameMap.getCurrentPlayer();
                    getPosition(player);


                    //CODE Ở ĐÂY
                    System.out.println("THE LENGTH OF SAFEZONE: " + gameMap.getSafeZone());
                    System.out.println("THE LENGTH OF MAPSIZE: " + gameMap.getMapSize());
                    System.out.println("Get Inventory: " + hero.getInventory());
                    System.out.println("Get List Gun in Inventory: " + hero.getInventory().getGun());
                    System.out.println("Get List Melee in Inventory: " + hero.getInventory().getMelee());
                    System.out.println("Get List Throwable in Inventory: " + hero.getInventory().getThrowable());
                    System.out.println("Get List Special in Inventory: " + hero.getInventory().getSpecial());
                    System.out.println("Get List Armor in Inventory: " + hero.getInventory().getArmor());
                    System.out.println("Get List Helmet in Inventory: " + hero.getInventory().getHelmet());
                    System.out.println("Get List Support Item in Inventory: " + hero.getInventory().getListSupportItem());

                    System.out.println("Get List CHEST in MAP: " + gameMap.getListChests());
                    System.out.println("Get List Destructible in MAP: " + gameMap.getObstaclesByTag("DESTRUCTIBLE"));
                    System.out.println("Get List Indestructible in MAP: " + gameMap.getObstaclesByTag("INDESTRUCTIBLE"));
                    hero.move("");



                    //NHẶT HEALING ITEM
//                    if (hero.getInventory().getListHealingItem().isEmpty()) {
//                        System.out.println("Không có Item. Đi kiếm thôi !!!");
//                        if (getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE) != null) {
//                            if (getDistance(gameMap, player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE)) == 0)
//                                hero.pickupItem();
//                            else
//                                hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE)));
//                        }
//                        else {
//                            if (getDistance(gameMap, player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.CHEST)) == 1)
//                                        hero.attack(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.CHEST)));
//                                    else
//                                        hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.CHEST)));
//                        }
//                    }




//                    //NHẶT MELEE
//                    if (hero.getInventory().getMelee().getId().equals("HAND") ) {
//                        System.out.println("Không có Melee. Đi kiếm thôi !!!");
//                        if (getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE) != null) {
//                            System.out.println("Có Melee trên Map. Đi nhặt thôi!!!");
//                            if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE)) == 0)
//                                hero.pickupItem();
//                            else
//                                hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE)));
//                        }
//                        else {
//                            System.out.println("Không có Melee trên Map. Đi đấm rương thôi!!!");
//                            if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.CHEST)) == 1)
//                                hero.attack(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.CHEST)));
//                            else
//                                hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.CHEST)));
//                        }
//                    }


//                    System.out.println("Get List Healing Item in Map: " + gameMap.getListHealingItems());
//                    System.out.println("Get List Armor/Helmet Item in Map: " + gameMap.getListArmors());
//                    System.out.println("Get List Weapon in Map: " + gameMap.getListWeapons());
//                    System.out.println("Get List Bullet in Map: " + gameMap.getListBullets());




//                            if (!player.getInventory().getListHealingItem().isEmpty()) {
//                                System.out.println("Item in inventory: " + player.getInventory().getListHealingItem());
//                                hero.useItem(player.getInventory().getListHealingItem().get(0).getId());
//                            } else {
//                                System.out.println("LIST HEALING ITEM IN MAP: " +getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE));
//                                if (getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE) != null) {
//                                    System.out.println("Cố gắng tìm vị trí Healing Item");
//                                    if (player.x == getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE).x && player.y == getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE).y)
//                                        hero.pickupItem();
//                                    else
//                                        hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE)));
//                                } else {
//                                    if (getDistance(gameMap, player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.CHESS)) == 1)
//                                        hero.attack(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.CHESS)));
//                                    else
//                                        hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.CHESS)));




                    // Tìm đến Chest gần nhất
                    //                    if (getDistance(gameMap, player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.CHESS)) == 1) {
                    //                        hero.attack (getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestChestInSafezone(gameMap,player)));
                    //                    }
                    //                    else
                    //                        hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestChestInSafezone(gameMap,player)));


                    // Tìm đến Shotgun gần nhất
                    //                    if (player.getInventory().getGun() == null)
                    //                        getNearestWeaponInSafezoneByID(gameMap,player,"SHOTGUN");
                    //                    else {
                    //                        if (getDistance(gameMap, player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.PLAYER)) == 1)
                    //                            hero.shoot(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.PLAYER)));
                    //                        else
                    //                            hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.PLAYER)));
                    //                    }


                } catch(IOException ex)

                {
                    System.err.println("Error" + ex);
                }


            }
        }


                ;
        hero.setOnMapUpdate(onMapUpdate);
        hero.start(SERVER_URL);
    }


    // Lấy vị trí hiện tại của Element
    public static void getPosition(Node node) {
        System.out.println("POSITION: " + node.x + "/" + node.y);
    }


    // Lấy Element gần nhất nằm trong Safezone bằng ElementType
    public static Element getNearestTargetInSafezoneByElementType(GameMap gameMap, Player player, ElementType elementType) {
        Element target = null;
        if (elementType.equals(ElementType.PLAYER)) {
            List<Player> listPlayer = gameMap.getOtherPlayerInfo();
            double minDistance = Double.MAX_VALUE;
            for (Player p : listPlayer) {
                double distance = PathUtils.distance(player, p);
                if (distance < minDistance && PathUtils.checkInsideSafeArea(p, gameMap.getSafeZone(), gameMap.getMapSize())) {
                    target = p;
                    minDistance = distance;
                }
            }
        }



        if (elementType.equals(ElementType.GUN)) {
            List<Weapon> listGun = gameMap.getAllGun();
            double minDistance = Double.MAX_VALUE;
            for (Weapon g : listGun) {
                double distance = PathUtils.distance(player, g);
                if (distance < minDistance && PathUtils.checkInsideSafeArea(g, gameMap.getSafeZone(), gameMap.getMapSize())) {
                    target = g;
                    minDistance = distance;
                }
            }
        }




        if (elementType.equals(ElementType.MELEE)) {
            List<Weapon> listMelee = gameMap.getAllMelee();
            double minDistance = Double.MAX_VALUE;
            for (Weapon g : listMelee) {
                double distance = PathUtils.distance(player, g);
                if (distance < minDistance && PathUtils.checkInsideSafeArea(g, gameMap.getSafeZone(), gameMap.getMapSize())) {
                    target = g;
                    minDistance = distance;
                }
            }
        }


        if (elementType.equals(ElementType.ALLY)) {
            List<Ally> listAlly = gameMap.getListAllies();
            double minDistance = Double.MAX_VALUE;
            for (Ally a : listAlly) {
                double distance = PathUtils.distance(player, a);
                if (distance < minDistance && PathUtils.checkInsideSafeArea(a, gameMap.getSafeZone(), gameMap.getMapSize())) {
                    target = a;
                    minDistance = distance;
                }
            }
        }

        if (elementType.equals(ElementType.HEALING_ITEM)) {
            List<SupportItem> listHealingItems = gameMap.getListSupportItems();
            double minDistance = Double.MAX_VALUE;
            for (SupportItem a : listHealingItems) {
                double distance =PathUtils.distance(player, a);
                if (distance < minDistance && PathUtils.checkInsideSafeArea(a, gameMap.getSafeZone(), gameMap.getMapSize())) {
                    target = a;
                    minDistance = distance;
                }
            }
        }

        if (elementType.equals(ElementType.CHEST)) {
            List<Obstacle> listChest = gameMap.getListChests();
            double minDistance = Double.MAX_VALUE;
            for (Obstacle c : listChest) {
                double distance = PathUtils.distance(player, c);
                if (distance < minDistance && PathUtils.checkInsideSafeArea(c, gameMap.getSafeZone(), gameMap.getMapSize())) {
                    target = c;
                    minDistance = distance;
                }
            }
        }
        return target;

    }
    //Lấy Weapon gần nhất nằm trong SafeZone bằng ID
    public static Element getNearestWeaponInSafezoneByID(GameMap gameMap, Player player, String ID) {
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
            double distance =PathUtils.distance(player, g);
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
        System.out.println("Player: " + player);
        System.out.println("Target: " + target);

        path = PathUtils.getShortestPath(gameMap, listRestrictedNode, player, target, true);
        System.out.println("PATH: " + path);
        return String.valueOf(path.charAt(0));
    }


    //Setup các List
    public static void setupAllList(List<Enemy> listEnemies, List<Obstacle> listIndestructibles, List<Obstacle> listObstacles, List<Obstacle> listChests, List<Obstacle> listTraps, List<Obstacle> listCanGoThroughObstacles, List<Obstacle> listCannotGoThroughObstacles, List<Element> listRestricted, List<Node> listRestrictedNode) {
        System.out.println("LIST ENEMIES: " + listEnemies);
        System.out.println("LIST OBSTACLES: " + listObstacles);
//        System.out.println("LIST INDESTRUCTIBLES: " + listIndestructibles);
//        System.out.println("LIST CHEST: " + listChests);
//        System.out.println("LIST TRAPS: " + listTraps);
        for (Obstacle o : listObstacles) {
            List<ObstacleTag> listTag = o.getTags();
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
        listRestricted.addAll(listCannotGoThroughObstacles);
        listRestrictedNode.addAll(listCannotGoThroughObstacles);
        System.out.println("LIST RESTRICTED (CHƯA NÉ TRAPS): " + listRestricted);
//        System.out.println("LIST RESTRICTED NODE (CHƯA NÉ TRAPS): " + listRestrictedNode);
    }

    //Tìm và đấm CHEST
    public static void findAndDestroyChess(Hero hero, GameMap gameMap, Player player, List<Node> listRestrictedNode) {
        try {
            hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap,player,ElementType.CHEST)));
            if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.CHEST)) == 1) {
                hero.attack(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap,player,ElementType.CHEST)));
            }
        } catch (IOException ex) {
            System.err.println("Error" + ex);
        }
    }
    //Nhặt MELEE khi có rương/melee ở gần
    public static void pickUpMeleeIfNearChestOrMelee (GameMap gameMap, Player player, Hero hero, List<Node> listRestrictedNode) throws IOException {
        if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE)) <= 3) {
            System.out.println("Ô có melee ở gần");
            if (hero.getInventory().getMelee().getId().equals("HAND")) {
                System.out.println("Mình không có melee. Đi nhặt thôi");
                if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE)) == 0) {
                    System.out.println("Melee dưới chân rồi. Nhặt thôi");
                    hero.pickupItem();
                    return;
                }
                else {
                    System.out.println("Melee cách vài bước. Lết đến thôi");
                    hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE)));
                    return;
                }
            }
        }
        else if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.CHEST)) <= 4) {
            System.out.println("Ô mình chưa có melee mà có rương cách đây 5 bước, đi nhặt thôi");
            findAndDestroyChess(hero,gameMap,player,listRestrictedNode);
        }
    }

    public static void pickUpArmorIfNearArmor (GameMap gameMap, Player player, Hero hero, List<Node> listRestrictedNode) throws IOException {
        if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.ARMOR)) <= 3) {
            System.out.println("Ô có giáp ở gần");
            if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.ARMOR)) == 0) {
                System.out.println("Giáp dưới chân rồi. Nhặt thôi");
                hero.pickupItem();
                return;
            }
            else {
                System.out.println("Giáp cách vài bước. Lết đến thôi");
                hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.ARMOR)));
                return;
            }
        }
    }

    public static void pickUpHelmetIfNearHelmet (GameMap gameMap, Player player, Hero hero, List<Node> listRestrictedNode) throws IOException {
        if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.HELMET)) <= 3) {
            System.out.println("Ô có mũ ở gần");
            if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.HELMET)) == 0) {
                System.out.println("Mũ dưới chân rồi. Nhặt thôi");
                hero.pickupItem();
                return;
            }
            else {
                System.out.println("Mũ cách vài bước. Lết đến thôi");
                hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.HELMET)));
                return;
            }
        }
    }


    //Nhặt HEALING ITEM NẾU GẦN RƯƠNG HOẶC HEALING ITEM
    public static void pickUpHealingItemIfNearChestOrHealingItem (GameMap gameMap, Player player, Hero hero, List<Node> listRestrictedNode) throws IOException {
        if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.HEALING_ITEM)) <= 3) {
            if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.HEALING_ITEM)) == 0) {
                hero.pickupItem();
                return;
            }
            else {
                hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.HEALING_ITEM)));
                return;
            }
        }
    }

    public static void findAndPickUpGun (GameMap gameMap, Player player, Hero hero, List<Node> listRestrictedNode) throws IOException {
        if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap,player,ElementType.GUN)) == 0) {
            hero.pickupItem();
        }
        else {
            hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.GUN)));
        }
    }

    public static void findAndPickUpMelee (GameMap gameMap, Player player, Hero hero, List<Node> listRestrictedNode) throws IOException {
        if (getNearestTargetInSafezoneByElementType(gameMap,player,ElementType.MELEE) != null) {
            if (PathUtils.distance(player, getNearestTargetInSafezoneByElementType(gameMap,player,ElementType.MELEE)) == 0) {
                hero.pickupItem();
            }
            else {
                hero.move(getPathToNearestTarget(gameMap, player, listRestrictedNode, getNearestTargetInSafezoneByElementType(gameMap, player, ElementType.MELEE)));
            }
        }
        else {
            findAndDestroyChess(hero, gameMap,player,listRestrictedNode);
        }
    }


}





