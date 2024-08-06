package jsclub.codefest2024.sdk.socket;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.model.Inventory;
import jsclub.codefest2024.sdk.socket.event_handler.onMapInit;
import jsclub.codefest2024.sdk.socket.event_handler.onPlayerInventoryUpdate;
import jsclub.codefest2024.sdk.util.SocketUtil;

public class SocketClient {
    private Socket socket;
    private final Inventory heroInventory;
    private final GameMap gameMap;
    
    public void connectToServer(String serverUrl, Emitter.Listener onMapUpdate) {
        if (socket != null) {
            socket.disconnect();
            socket = null;
        }

        socket = SocketUtil.init(serverUrl);
        if (socket == null) {
            return;
        }

        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        final boolean[] connected = {false};

        while (endTime - startTime < 120 * 1000 && connected[0] == false) {
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    connected[0] = true;
                    System.out.println("Connected to the server");
                    socket.on(EventName.ON_MAP_INIT, new onMapInit(gameMap));
                    socket.on(EventName.ON_INVENTORY_UPDATE, new onPlayerInventoryUpdate(heroInventory));

                    socket.on(EventName.ON_MAP_UPDATE, onMapUpdate);
                }
            });

            endTime = System.currentTimeMillis();
        }

        if (!connected[0]) {
            System.out.println("Disconnected from the server");
            return;
        }

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
