package jsclub.codefest.sdk.socket.event_handler;

import com.google.gson.Gson;
import io.socket.emitter.Emitter;
import jsclub.codefest.sdk.Constants;
import jsclub.codefest.sdk.model.GameMap;
import jsclub.codefest.sdk.model.effects.Effect;
import jsclub.codefest.sdk.socket.data.receive_data.EffectData;
import jsclub.codefest.sdk.util.MsgPackUtil;

import java.io.IOException;
import java.util.List;

public class onPlayerEffectApply implements Emitter.Listener {
    private final List<Effect> effects;
    private final GameMap gameMap;
    Gson gson = new Gson();

    public onPlayerEffectApply(List<Effect> effects, GameMap gameMap) {
        this.effects = effects;
        this.gameMap = gameMap;
    }

    @Override
    public void call(Object... args) {
        try {
            String message = MsgPackUtil.decode(args[0]);
            EffectData effectData = gson.fromJson(message, EffectData.class);
            System.out.println("Effect applied: " + effectData.effect);
            
            // Calculate affectedAt and estimatedEndAt
            int currentStepNumber = gameMap.getStepNumber();
            effectData.effect.setAffectedAt(currentStepNumber);
            effectData.effect.setEstimatedEndAt(currentStepNumber + effectData.effect.duration);
            
            effects.add(effectData.effect);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
