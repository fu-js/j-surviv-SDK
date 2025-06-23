package jsclub.codefest.sdk.socket.event_handler;

import com.google.gson.Gson;
import io.socket.emitter.Emitter;
import jsclub.codefest.sdk.model.effects.Effect;
import jsclub.codefest.sdk.socket.data.receive_data.EffectApply;
import jsclub.codefest.sdk.util.MsgPackUtil;

import java.io.IOException;
import java.util.Map;

public class onPlayerEffectApply implements Emitter.Listener {
    Gson gson = new Gson();

    @Override
    public void call(Object... args) {
        try {
            String message = MsgPackUtil.decode(args[0]);
            System.out.println("Effect applied:  "+ message);
//            Map<String, Effect> effectApply = gson.fromJson(message, Map.class);
//            Effect value = effectApply.get("effect");
//            System.out.println("Effect applied: "+ value);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
