package jsclub.codefest2024.sdk.socket.event_handler;

import io.socket.emitter.Emitter;
import jsclub.codefest2024.sdk.model.GameMap;

public class onMapInit implements Emitter.Listener {
    private final GameMap gameMap;

    public onMapInit(GameMap gameMap) {
        System.out.println("onMapInit");
        this.gameMap = gameMap;
    }

    @Override
    public void call(Object... args) {
        gameMap.updateOnInitMap(args[0]);
    }
}
