package jsclub.codefest2024.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import jsclub.codefest2024.socket.data.Game;
import jsclub.codefest2024.sdk.util.SocketUtil;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Hero {
    private static final Logger LOGGER = LogManager.getLogger(Hero.class);
    private String playerID = "";
    private String gameID = "";
    private Socket socket;
    private Emitter.Listener onTickTackListener = objects -> {
    };

    public Hero(String playerID, String gameID) {
        this.playerID = playerID;
        this.gameID = gameID;
    }

    public String getPlayerID() {
        return playerID;
    }

    public String getGameID() {
        return gameID;
    }

    public void getPlayerInfo() {
        if (socket != null) {
            Player player = new Player();
            LOGGER.debug("Player = {} - PlayerInfo = {}", this.playerID, player);
            try {
                socket.emit(ServerSocketConfig.PLAYER_INFO, new JSONObject(player.toString()));
            } catch (JSONException e) {
                LOGGER.error(e);
            }
        }
    }

    public void move(String step) {
        if (socket != null && step.length() > 0) {
            Dir dir = new Dir(step);
            LOGGER.debug("Player = {} - Dir = {}", this.playerID, dir);
            try {
                socket.emit(ServerSocketConfig.DRIVE_PLAYER, new JSONObject(dir.toString()));
            } catch (JSONException e) {
                LOGGER.error(e);
            }
        }
    }

    public Boolean connectToServer(String serverUrl) {
        if (socket != null) {
            socket.disconnect();
            socket = null;
        }

        socket = SocketUtil.init(serverUrl);

        if (socket == null) {
            LOGGER.error("Socket null - can't connect");
            return false;
        }

        socket.on(Socket.EVENT_CONNECT, object -> {
            String gameParams = new Game(gameID, playerID).toString();
            try {
                socket.emit(ServerSocketConfig.JOIN_GAME, new JSONObject(gameParams));
                LOGGER.info("{} connected into game {}!", this.playerID, this.gameID);
            } catch (JSONException e) {
                LOGGER.error(e);
            }
        });

        socket.on(ServerSocketConfig.TICKTACK_PLAYER, onTickTackListener);
        socket.on(Socket.EVENT_CONNECT_ERROR, objects -> LOGGER.error("Connect Failed " + objects[0].toString()));
        socket.on(Socket.EVENT_DISCONNECT, objects -> LOGGER.info("{} Disconnected!", this.playerID));

        socket.connect();
        return true;
    }
}
