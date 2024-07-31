package jsclub.codefest2024.bot;

import jsclub.codefest2024.sdk.model.Hero;
import io.socket.emitter.Emitter.Listener;
import jsclub.codefest2024.sdk.socket.data.Map.MapInit;

public class Main {
    private static final String SERVER_URL = "https://cf-server.jsclub.dev";

    public static void main(String[] args) {
        Hero hero = new Hero("player1", "game1");
        hero.start(SERVER_URL);

        Listener onMapUpdate = objects -> {

        };

        hero.setOnMapUpdate(onMapUpdate);
    }
}
