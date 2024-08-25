package jsclub.codefest2024.sdk.socket;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import jsclub.codefest2024.sdk.Hero;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.model.Inventory;
import jsclub.codefest2024.sdk.socket.event_handler.onMapInit;
import jsclub.codefest2024.sdk.socket.event_handler.onPlayerInventoryUpdate;
import jsclub.codefest2024.sdk.util.SocketUtil;

public class SocketClient {
    private Socket socket;
    private final Inventory heroInventory;
    private final GameMap gameMap;

    public void connectToServer(String serverUrl, Emitter.Listener onMapUpdate, Hero hero) {
        if (socket != null) {
            socket.disconnect();
            socket = null;
        }

        socket = SocketUtil.init(serverUrl);
        if (socket == null) {
            return;
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Connected to the server");
                socket.on(EventName.ON_MAP_INIT, new onMapInit(gameMap));
                socket.on(EventName.ON_INVENTORY_UPDATE, new onPlayerInventoryUpdate(hero.getInventory()));

                socket.on(EventName.ON_MAP_UPDATE, onMapUpdate);
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Disconnected from the server");
            }
        });

        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.err.println("Connection error: " + args[0]);
            }
        });

        socket.connect();
    }

    public Socket getSocket() {
        return socket;
    }

    public SocketClient(Inventory heroInventory, GameMap gameMap) {
        this.heroInventory = heroInventory;
        this.gameMap = gameMap;
    }
}
