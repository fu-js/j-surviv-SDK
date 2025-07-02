package jsclub.codefest.sdk;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import jsclub.codefest.sdk.base.Node;
import jsclub.codefest.sdk.factory.SupportItemFactory;
import jsclub.codefest.sdk.model.GameMap;
import jsclub.codefest.sdk.model.Inventory;
import jsclub.codefest.sdk.model.effects.Effect;
import jsclub.codefest.sdk.model.support_items.SupportItem;
import jsclub.codefest.sdk.socket.EventName;
import jsclub.codefest.sdk.socket.SocketClient;
import jsclub.codefest.sdk.socket.data.emit_data.*;
import jsclub.codefest.sdk.util.MsgPackUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Hero {
    private String playerName = "";
    private String gameID = "";
    private String secretKey = "";
    private final SocketClient socketClient;
    private final GameMap gameMap;
    private final Inventory inventory;
    private final List<Effect> effects;
    private Emitter.Listener onMapUpdate;

    public Hero(String gameID, String playerName, String secretKey) {
        this.playerName = playerName;
        this.gameID = gameID;
        this.secretKey = secretKey;
        this.inventory = new Inventory();
        this.effects = new ArrayList<>();
        this.gameMap = new GameMap(this.getInventory(), this.getEffects());
        this.socketClient = new SocketClient(this.inventory, this.effects, this.gameMap);
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void start(String serverURL) throws IOException {
        if (this.onMapUpdate == null) {
            throw new RuntimeException("onMapUpdate is not set");
        }

        if (this.playerName.isEmpty()) {
            throw new RuntimeException("playerName is not set");
        }

        if (this.gameID.isEmpty()) {
            throw new RuntimeException("gameID is not set");
        }

        socketClient.connectToServer(serverURL, playerName, secretKey, onMapUpdate)
                .thenRun(this::joinGame)
                .exceptionally(ex -> {
                    System.err.println("Failed to connect and join game: " + ex.getMessage());
                    ex.printStackTrace();
                    return null;
                });
    }

    public String getPlayerID() {
        return playerName;
    }

    public String getGameID() {
        return gameID;
    }

    public void joinGame() {
        Socket socket = socketClient.getSocket();

        if (socket != null) {
            try {
                PlayerJoinGameAction joinGame = new PlayerJoinGameAction(this.gameID);
                byte[] bytes = MsgPackUtil.encodeFromObject(joinGame);
                socket.emit(EventName.EMIT_JOIN_GAME, (Object) bytes);
            } catch (IOException e) {
                e.printStackTrace(); // or handle more gracefully
            }
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
            throw new IllegalArgumentException("Invalid direction");
        }

        if (socket != null) {
            PlayerMoveAction botMove = new PlayerMoveAction(direction);
            byte[] bytes = MsgPackUtil.encodeFromObject(botMove);
            socket.emit(EventName.EMIT_MOVE, (Object) bytes);
        } else {
            throw new IllegalStateException("Socket is null");
        }
    }


    /**
     * Shoots a projectile in the specified direction ('l', 'r', 'u', 'd').
     *
     * @param direction the direction in which to shoot
     * @throws IOException if an I/O error occurs
     */
    public void useSpecial(String direction) throws IOException{
        Socket socket = socketClient.getSocket();

        if (direction.isEmpty()) {
            throw new IllegalArgumentException("Direction is null or empty");
        }

        if (direction.length() != 1) {
            throw new IllegalArgumentException("Direction string length must be 1");
        }

        if (invalidDirection(direction)) {
            throw new IllegalArgumentException("Invalid direction");
        }

        if (socket == null) {
            throw new IllegalStateException("Socket is null or inventory does not have special weapons");
        }

        PlayerUseSpecialAction botUseSpecial = new PlayerUseSpecialAction(direction);
        byte[] bytes = MsgPackUtil.encodeFromObject(botUseSpecial);
        socket.emit(EventName.EMIT_USE_SPECIAL, (Object)bytes);

    }

    public void shoot(String direction) throws IOException {
        Socket socket = socketClient.getSocket();

        if (direction.isEmpty() || direction == null) {
            throw new IllegalArgumentException("Direction is null or empty");
        }

        if (direction.length() != 1) {
            throw new IllegalArgumentException("Direction string length must be 1");
        }

        if (invalidDirection(direction)) {
            throw new IllegalArgumentException("Invalid direction");
        }

        if (socket == null || getInventory().getGun() == null) {
            throw new IllegalStateException("Socket is null or inventory does not have gun");
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
            throw new IllegalArgumentException("Direction is null or empty");
        }

        if (direction.length() != 1) {
            throw new IllegalArgumentException("Direction string length must be 1");
        }

        if (invalidDirection(direction)) {
            throw new IllegalArgumentException("Invalid direction");
        }

        if (socket == null) {
            throw new IllegalStateException("Socket is null");
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
            throw new IllegalArgumentException("Direction is null or empty");
        }
        if (direction.length() != 1) {
            throw new IllegalArgumentException("Direction string length must be 1");
        }
        if (invalidDirection(direction)) {
            throw new IllegalArgumentException("Invalid direction");
        }

        if (socket == null || getInventory().getThrowable() == null) {
            throw new IllegalStateException("Socket is null or inventory does not have throwable");
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

        //System.out.println("hasItem:"+hasItem);

        if (socket == null || !hasItem) {
            throw new IllegalStateException("Socket is null or current position does not have item");
        }

        String data = "{}";
        byte[] bytes = MsgPackUtil.encodeFromObject(data);
        socket.emit(EventName.EMIT_PICKUP_ITEM, (Object) bytes);
    }

    private boolean hasItem(int x, int y) {
        List<Node> listItem = new ArrayList<>();
        listItem.addAll(getGameMap().getListSupportItems());
        listItem.addAll(getGameMap().getAllGun());
        listItem.addAll(getGameMap().getAllMelee());
        listItem.addAll(getGameMap().getAllThrowable());
        listItem.addAll(getGameMap().getAllSpecial());
        listItem.addAll(getGameMap().getListArmors());
        listItem.addAll(getGameMap().getAllSpecial());

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
        SupportItem item = SupportItemFactory.getSupportItemById(itemId);
        int indexOfItem = getInventory().getListSupportItem().indexOf(item);

        if (itemId.isEmpty() || itemId == null) {
            throw new IllegalArgumentException("ItemId is null or empty");
        }
        if (indexOfItem == -1) {
            throw new IllegalArgumentException("Inventory does not have " + item.getId());
        }

        if (socket == null || getInventory().getListSupportItem().get(indexOfItem) == null) {
            throw new IllegalStateException("Socket is null or cannot get item");
        }

        List<SupportItem> inventAfter = inventory.getListSupportItem();
        inventAfter.remove(indexOfItem);
        inventory.setListSupportItem(inventAfter);
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
            throw new IllegalArgumentException("ItemId is null or empty");
        }

        if(socket == null){
            throw new IllegalStateException("Socket is null");
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
