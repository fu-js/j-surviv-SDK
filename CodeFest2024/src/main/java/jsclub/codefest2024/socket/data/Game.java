package jsclub.codefest2024.socket.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Game {
    public final String game_id;
    public final String player_id;

    public Game(String gameId, String playerId) {
        this.game_id = gameId;
        this.player_id = playerId;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(this);
    }
}