package jsclub.codefest.sdk.socket.data.receive_data;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import jsclub.codefest.sdk.model.obstacles.Obstacle;
import jsclub.codefest.sdk.model.players.Player;

import java.util.List;

public class MapData {
    @SerializedName("mapSize")
    public int mapSize;

    @SerializedName("obstacles")
    public List<Obstacle> listObstacles;

    @SerializedName("safeZoneRadius")
    public int safeZone;
    
    @SerializedName("isAccentWallOpened")
    public boolean isAccentWallOpened;

    @SerializedName("entities")
    public List<Entity> listEntities;

    @SerializedName("otherPlayers")
    public List<Player> otherPlayers;

    @SerializedName("currentPlayer")
    public Player currentPlayer;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
