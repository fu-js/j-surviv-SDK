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
        for (int i = 0; i < direction.length(); i++) {
            char ch = direction.charAt(i);
            if (ch != 'u' && ch != 'd' && ch != 'l' && ch != 'r') {
                return true;
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

        if (direction.isEmpty()) {
            throw new RuntimeException("Direction cannot be null");
        } else if (invalidDirection(direction)) {
            throw new RuntimeException("Invalid direction");
        } else if (socket != null) {
            PlayerMoveAction botMove = new PlayerMoveAction(direction);

            byte[] bytes = MsgPackUtil.encodeFromObject(botMove);
            socket.emit(EventName.EMIT_MOVE, (Object) bytes);
        } else {
            throw new RuntimeException("Socket is null");
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

        if (direction.isEmpty()) {
            throw new RuntimeException("direction cannot be null");
        } else if (direction.length() == 1) {
            if (invalidDirection(direction)) {
                throw new RuntimeException("Invalid direction");
            } else if (socket != null && getInventory().getGun() != null) {
                PlayerShootAction botShoot = new PlayerShootAction(direction);

                byte[] bytes = MsgPackUtil.encodeFromObject(botShoot);
                socket.emit(EventName.EMIT_SHOOT, (Object) bytes);
            } else {
                throw new RuntimeException("Socket is null or inventory does not have gun");
            }
        } else {
            throw new RuntimeException("direction string length must be 1");
        }


    }

    /**
     * Performs a melee attack in the specified direction ('l', 'r', 'u', 'd').
     *
     * @param direction the direction in which to attack
     * @throws IOException if an I/O error occurs
     */
    public void attack(String direction) throws IOException {
        Socket socket = socketClient.getSocket();

        if (direction.isEmpty()) {
            throw new RuntimeException("direction cannot be null");
        } else if (direction.length() == 1) {
            if (invalidDirection(direction)) {
                throw new RuntimeException("Invalid direction");
            } else if (socket != null) {
                PlayerAttackAction botAttack = new PlayerAttackAction(direction);

                byte[] bytes = MsgPackUtil.encodeFromObject(botAttack);
                socket.emit(EventName.EMIT_ATTACK, (Object) bytes);
            }
        } else {
            throw new RuntimeException("direction string length must be 1");
        }

    }

    /**
     * Throws a throwable object in the specified direction ('l', 'r', 'u', 'd').
     *
     * @param direction the direction in which to throw the object
     * @throws IOException if an I/O error occurs
     */
    public void throwItem(String direction) throws IOException {
        Socket socket = socketClient.getSocket();

        if (direction.isEmpty()) {
            throw new RuntimeException("direction cannot be null");
        } else if (direction.length() == 1) {
            if (invalidDirection(direction)) {
                throw new RuntimeException("Invalid direction");
            } else if (socket != null && getInventory().getThrowable() != null) {
                PlayerThrowItemAction botThrow = new PlayerThrowItemAction(direction);

                byte[] bytes = MsgPackUtil.encodeFromObject(botThrow);
                socket.emit(EventName.EMIT_THROW, (Object) bytes);
            } else {
                throw new RuntimeException("Socket is null or inventory does not have throwable");
            }
        } else {
            throw new RuntimeException("direction string length must be 1");
        }
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

        if (socket != null && hasItem) {
            String data = "{}";
            byte[] bytes = MsgPackUtil.encodeFromObject(data);
            socket.emit(EventName.EMIT_PICKUP_ITEM, (Object) bytes);
        } else {
            throw new RuntimeException("Socket is null or current position does not have item");
        }
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

        if (socket != null && getInventory().getListHealingItem().get(indexOfItem) != null) {
            PlayerUseItemAction botUseItem = new PlayerUseItemAction(itemId);

            byte[] bytes = MsgPackUtil.encodeFromObject(botUseItem);
            socket.emit(EventName.EMIT_USE_ITEM, (Object) bytes);
        } else if (itemId.isEmpty()) {
            throw new RuntimeException("itemId cannot be null");
        } else if (indexOfItem == -1) {
            throw new RuntimeException("Inventory does not have " + item.getId());
        } else {
            throw new RuntimeException("Socket is null or cannot get item");
        }
    }

    /**
     * Revokes an item with the specified ID.
     *
     * @param itemId the ID of the item to revoke
     * @throws IOException if an I/O error occurs
     */
    public void revokeItem(String itemId) throws IOException {
        Socket socket = socketClient.getSocket();
        HealingItem healingItem = HealingItemFactory.getHealingItemById(itemId);
        Armor armor = ArmorFactory.getArmorById(itemId);
        Weapon weapon = WeaponFactory.getWeaponById(itemId);
        ElementType type = weapon.getType();
        Weapon inventWeapon = null;
        if (type.equals(ElementType.GUN)) inventWeapon = getInventory().getGun();
        if (type.equals(ElementType.THROWABLE)) inventWeapon = getInventory().getThrowable();
        if (type.equals(ElementType.MELEE) && !weapon.getId().equals("HAND")) inventWeapon = getInventory().getMelee();
        int inventArmor = getInventory().getListArmor().indexOf(armor);
        int inventHeal = getInventory().getListHealingItem().indexOf(healingItem);

        if (inventWeapon != null || inventArmor > 0 || inventHeal > 0) {
            if (socket != null) {
                PlayerRevokeItemAction botRevokeItem = new PlayerRevokeItemAction(itemId);

                byte[] bytes = MsgPackUtil.encodeFromObject(botRevokeItem);
                socket.emit(EventName.EMIT_REVOKE_ITEM, (Object) bytes);
            } else if (itemId.isEmpty()) {
                throw new RuntimeException("itemId cannot be null");
            } else {
                throw new RuntimeException("Socket is null or cannot get item");
            }
        } else {
            throw new RuntimeException("Inventory not have " + itemId);
        }


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
