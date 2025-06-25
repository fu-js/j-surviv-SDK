package jsclub.codefest.sdk.socket.event_handler;

import com.google.gson.Gson;
import io.socket.emitter.Emitter;
import jsclub.codefest.sdk.factory.EffectFactory;
import jsclub.codefest.sdk.model.effects.Effect;
import jsclub.codefest.sdk.socket.data.receive_data.EffectClearData;
import jsclub.codefest.sdk.util.MsgPackUtil;

import java.io.IOException;
import java.util.List;

public class onPlayerEffectClear implements Emitter.Listener {
    private final List<Effect> effects;
    Gson gson = new Gson();

    public onPlayerEffectClear(List<Effect> effects) {
        this.effects = effects;
    }

    @Override
    public void call(Object... args) {
        try {
            String message = MsgPackUtil.decode(args[0]);
            //{"effectId":"STUN"}
            EffectClearData effect = gson.fromJson(message, EffectClearData.class);
            System.out.println("Effect cleared: " + effect.effectId);
            effects.removeIf(e -> e.id.equals(effect.effectId));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
