package jsclub.codefest2024.sdk.socket.data.emit_data;

import com.google.gson.annotations.SerializedName;

/**
 * emit_data to join game
 */
public class JoinGame {
    @SerializedName("game_id")
    private String gameID;

    @SerializedName("player_name")
    private String playerName;

    // Constructor
    public JoinGame(String gameID, String playerName) {
        this.gameID = gameID;
        this.playerName = playerName;
    }
}
