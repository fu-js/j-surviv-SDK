package jsclub.codefest2024.sdk;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import jsclub.codefest2024.sdk.base.Node;
import jsclub.codefest2024.sdk.factory.ArmorFactory;
import jsclub.codefest2024.sdk.factory.HealingItemFactory;
import jsclub.codefest2024.sdk.factory.WeaponFactory;
import jsclub.codefest2024.sdk.model.Element;
import jsclub.codefest2024.sdk.model.ElementType;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.model.Inventory;
import jsclub.codefest2024.sdk.model.equipments.Armor;
import jsclub.codefest2024.sdk.model.equipments.HealingItem;
import jsclub.codefest2024.sdk.model.weapon.Weapon;
import jsclub.codefest2024.sdk.socket.EventName;
import jsclub.codefest2024.sdk.socket.SocketClient;
import jsclub.codefest2024.sdk.socket.data.emit_data.*;
import jsclub.codefest2024.sdk.util.MsgPackUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hero {
    private String playerName = "";
    private String playerKey = "";
    private String gameID = "";
    private final SocketClient socketClient;
    private final GameMap gameMap;
    private final Inventory inventory;
    private Emitter.Listener onMapUpdate;

    public Hero(String gameID, String playerName, String playerKey) {
        this.playerName = playerName;
        this.playerKey = playerKey;
        this.gameID = gameID;
        this.inventory = new Inventory();
        this.gameMap = new GameMap(this.getInventory());
        this.socketClient = new SocketClient(this.inventory, this.gameMap);
    }

    public void start(String serverURL) throws IOException {
        if (this.onMapUpdate == null) {
            throw new RuntimeException("onMapUpdate is not set");
        }

        if (this.playerName.isEmpty()) {
            throw new RuntimeException("playerName is not set");
        }

        if (this.playerKey.isEmpty()) {
            throw new RuntimeException("playerKey is not set");
        }

        if (this.gameID.isEmpty()) {
            throw new RuntimeException("gameID is not set");
        }

        socketClient.connectToServer(serverURL, this.playerName, this.playerKey, this.onMapUpdate);
        this.joinGame();
    }

    public String getPlayerID() {
        return playerName;
    }

    public String getGameID() {
        return gameID;
    }

    public void joinGame() throws IOException {
        Socket socket = socketClient.getSocket();

        if (socket != null) {
            PlayerJoinGameAction joinGame = new PlayerJoinGameAction(this.gameID, this.playerName);
            byte[] bytes = MsgPackUtil.encodeFromObject(joinGame);
            socket.emit(EventName.EMIT_JOIN_GAME, (Object) bytes);
        }
    }

    private boolean invalidDirection(String direction) {
        if (direction == null) {
            return true;
        }

        int dirLength = direction.length();
        if (dirLength > 0) {
            for (int i = 0; i < direction.length(); i++) {
                char ch = direction.charAt(i);
                if (ch != 'u' && ch != 'd' && ch != 'l' && ch != 'r') {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Moves the player in the specified direction ('l', 'r', 'u', 'd').
     *
     * @param direction the direction in which to move the player
     * @throws IOException if an I/O error occurs
     */
    public void move(String direction) throws IOException {
        Socket socket = socketClient.getSocket();

        if (invalidDirection(direction)) {
            System.out.println("DEBUG FROM SDK move ERROR : Invalid direction");
            return;
        }

        if (socket != null) {
            PlayerMoveAction botMove = new PlayerMoveAction(direction);
            byte[] bytes = MsgPackUtil.encodeFromObject(botMove);
            socket.emit(EventName.EMIT_MOVE, (Object) bytes);
        } else {
            System.out.println("DEBUG FROM SDK move ERROR : Socket is null");
        }
    }

    /**
     * Shoots a projectile in the specified direction ('l', 'r', 'u', 'd').
     *
     * @param direction the direction in which to shoot
     * @throws IOException if an I/O error occurs
     */
    public void shoot(String direction) throws IOException {
        Socket socket = socketClient.getSocket();

        if (direction.isEmpty() || direction == null) {
            System.out.println("DEBUG FROM SDK shoot ERROR : direction is null or empty");
            return;
        }

        if (direction.length() != 1) {
            System.out.println("DEBUG FROM SDK shoot ERROR : direction string length must be 1");
            return;
        }

        if (invalidDirection(direction)) {
            System.out.println("DEBUG FROM SDK shoot ERROR : Invalid direction");
            return;
        }

        if (socket == null || getInventory().getGun() == null) {
            System.out.println("DEBUG FROM SDK shoot ERROR : Socket is null or inventory does not have gun");
            return;
        }

        PlayerShootAction botShoot = new PlayerShootAction(direction);
        byte[] bytes = MsgPackUtil.encodeFromObject(botShoot);
        socket.emit(EventName.EMIT_SHOOT, (Object) bytes);

    }


    /**
     * Performs a melee attack in the specified direction ('l', 'r', 'u', 'd').
     *
     * @param direction the direction in which to attack
     * @throws IOException if an I/O error occurs
     */
    public void attack(String direction) throws IOException {
        Socket socket = socketClient.getSocket();

        if (direction.isEmpty() || direction == null) {
            System.out.println("DEBUG FROM SDK attack ERROR : direction is null or empty");
            return;
        }

        if (direction.length() != 1) {
            System.out.println("DEBUG FROM SDK attack ERROR : direction string length must be 1");
            return;
        }

        if (invalidDirection(direction)) {
            System.out.println("DEBUG FROM SDK attack ERROR : Invalid direction");
            return;
        }

        if (socket == null) {
            System.out.println("DEBUG FROM SDK shoot ERROR : Socket is null");
            return;
        }

        PlayerAttackAction botAttack = new PlayerAttackAction(direction);
        byte[] bytes = MsgPackUtil.encodeFromObject(botAttack);
        socket.emit(EventName.EMIT_ATTACK, (Object) bytes);
    }

    /**
     * Throws a throwable object in the specified direction ('l', 'r', 'u', 'd').
     *
     * @param direction the direction in which to throw the object
     * @throws IOException if an I/O error occurs
     */
    public void throwItem(String direction) throws IOException {
        Socket socket = socketClient.getSocket();

        if (direction.isEmpty() || direction == null) {
            System.out.println("DEBUG FROM SDK throwItem ERROR : direction is null or empty");
            return;
        }
        if (direction.length() != 1) {
            System.out.println("DEBUG FROM SDK throwItem ERROR : direction string length must be 1");
            return;
        }
        if (invalidDirection(direction)) {
            System.out.println("DEBUG FROM SDK throwItem ERROR : Invalid direction");
            return;
        }

        if (socket != null || getInventory().getThrowable() != null) {
            System.out.println("DEBUG FROM SDK throwItem ERROR : Socket is null or inventory does not have throwable");
            return;
        }

        PlayerThrowItemAction botThrow = new PlayerThrowItemAction(direction);
        byte[] bytes = MsgPackUtil.encodeFromObject(botThrow);
        socket.emit(EventName.EMIT_THROW, (Object) bytes);
    }


    /**
     * Picks up an item at the player's current position.
     *
     * @throws IOException if an I/O error occurs
     */
    public void pickupItem() throws IOException {
        Socket socket = socketClient.getSocket();

        Node currentPos = new Node(getGameMap().getCurrentPlayer().x, getGameMap().getCurrentPlayer().y);
        boolean hasItem = hasItem(currentPos.x, currentPos.y);

        if (socket == null || !hasItem) {
            System.out.println("DEBUG FROM SDK pickupItem ERROR : Socket is null or current position does not have item");
            return;
        }

        String data = "{}";
        byte[] bytes = MsgPackUtil.encodeFromObject(data);
        socket.emit(EventName.EMIT_PICKUP_ITEM, (Object) bytes);
    }

    private boolean hasItem(int x, int y) {
        List<Node> listItem = new ArrayList<>();
        listItem.addAll(getGameMap().getListHealingItems());
        listItem.addAll(getGameMap().getAllGun());
        listItem.addAll(getGameMap().getAllMelee());
        listItem.addAll(getGameMap().getAllThrowable());
        listItem.addAll(getGameMap().getListArmors());

        boolean hasItem = false;

        for (Node item : listItem) {
            if (item.x == x && item.y == y) {
                hasItem = true;
                break;
            }
        }
        return hasItem;
    }

    /**
     * Uses an item with the specified ID.
     *
     * @param itemId the ID of the item to use
     * @throws IOException if an I/O error occurs
     */
    public void useItem(String itemId) throws IOException {
        Socket socket = socketClient.getSocket();
        HealingItem item = HealingItemFactory.getHealingItemById(itemId);
        int indexOfItem = getInventory().getListHealingItem().indexOf(item);

        if (itemId.isEmpty() || itemId == null) {
            System.out.println("DEBUG FROM SDK useItem ERROR : itemId is null or empty");
            return;
        }
        if (indexOfItem == -1) {
            System.out.println("DEBUG FROM SDK useItem ERROR : Inventory does not have " + item.getId());
            return;
        }

        if (socket == null || getInventory().getListHealingItem().get(indexOfItem) == null) {
            System.out.println("DEBUG FROM SDK useItem ERROR : Socket is null or cannot get item");
            return;
        }

        List<HealingItem> inventAfter = inventory.getListHealingItem();
        inventAfter.remove(indexOfItem);
        inventory.setListHealingItem(inventAfter);
        PlayerUseItemAction botUseItem = new PlayerUseItemAction(itemId);

        byte[] bytes = MsgPackUtil.encodeFromObject(botUseItem);
        socket.emit(EventName.EMIT_USE_ITEM, (Object) bytes);
    }

    /**
     * Revokes an item with the specified ID.
     *
     * @param itemId the ID of the item to revoke
     * @throws IOException if an I/O error occurs
     */
    public void revokeItem(String itemId) throws IOException {
        Socket socket = socketClient.getSocket();

        if (itemId.isEmpty() || itemId == null) {
            System.out.println("DEBUG FROM SDK revokeItem ERROR : itemId is null or empty");
            return;
        }

        if(socket == null){
            System.out.println("DEBUG FROM SDK revokeItem ERROR : Socket is null");
            return;
        }

        PlayerRevokeItemAction botRevokeItem = new PlayerRevokeItemAction(itemId);
        byte[] bytes = MsgPackUtil.encodeFromObject(botRevokeItem);
        socket.emit(EventName.EMIT_REVOKE_ITEM, (Object) bytes);
    }

    public String getPlayerName() {
        return playerName;
    }

    /**
     * Retrieves the current game map information.
     *
     * @return the current game map
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    public void setOnMapUpdate(Emitter.Listener onMapUpdate) {
        this.onMapUpdate = onMapUpdate;
    }

    /**
     * Retrieves the player's inventory information.
     *
     * @return the player's inventory
     */
    public Inventory getInventory() {
        return inventory;
    }
}
