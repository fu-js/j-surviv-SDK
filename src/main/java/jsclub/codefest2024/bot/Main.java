package jsclub.codefest2024.bot;

import io.socket.emitter.Emitter;
import jsclub.codefest2024.sdk.algorithm.ShortestPath;
import jsclub.codefest2024.sdk.model.GameMap;
import jsclub.codefest2024.sdk.Hero;
import jsclub.codefest2024.sdk.model.players.Player;
import jsclub.codefest2024.sdk.model.weapon.Weapon;

import java.io.IOException;
import java.util.List;

public class Main {
    private static final String SERVER_URL = "https://cf-server.jsclub.dev";
    private static final String GAME_ID = "170409";
    private static final String PLAYER_NAME = "ptd";

    public static String randomMove() {
        String[] moves = {"u", "d", "l", "r"};
        return moves[(int) (Math.random() * moves.length)];
    }

    public static void main(String[] args) throws IOException {
        Hero hero = new Hero(GAME_ID, PLAYER_NAME);

        Emitter.Listener onMapUpdate = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    GameMap gameMap = hero.getGameMap();
                    gameMap.updateOnUpdateMap(args[0]);

                    Player player = gameMap.getCurrentPlayer();
                    Weapon target = getNearestWeapon(gameMap, player);

                    hero.move(ShortestPath.getShortestPath(
                            gameMap,
                            List.of(),
                            player,
                            target,
                            false
                    ));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        hero.setOnMapUpdate(onMapUpdate);
        hero.start(SERVER_URL);
    }

    private static Weapon getNearestWeapon(GameMap gameMap, Player player) {
        List<Weapon> guns = gameMap.getAllGun();
        Weapon target = guns.get(0);
        double distance = 10000000;
        for (Weapon weapon : guns) {
            if (distance > (player.x - weapon.x) * (player.x - weapon.x)
                    + (player.y - weapon.y) * (player.y - weapon.y))
            {
                distance = (player.x - weapon.x) * (player.x - weapon.x)
                        + (player.y - weapon.y) * (player.y - weapon.y);
                target = weapon;
            }
        }
        return target;
    }
}