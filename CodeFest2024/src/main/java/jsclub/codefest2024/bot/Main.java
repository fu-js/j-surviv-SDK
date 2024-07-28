package jsclub.codefest2024.bot;

import jsclub.codefest2024.model.Hero;

public class Main {
    final static String SERVER_URL = "https://cf-server.jsclub.dev/sdk";

    public static void main(String[] args) {
        // Creating a new Hero object with name `player1-xxx` and game id
        // `GameConfig.GAME_ID`.
        Hero player1 = new Hero("player1-xxx", "da2d6f34-47a4-4470-9503-b134038e2247");

        // This is the code that connects the player to the server.
        player1.connectToServer(SERVER_URL);
    }
}
