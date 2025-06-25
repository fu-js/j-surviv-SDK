package jsclub.codefest.sdk.socket.data.receive_data;

import com.google.gson.annotations.SerializedName;
import jsclub.codefest.sdk.model.effects.Effect;

public class EffectData {
    @SerializedName("effect")
    public Effect effect;
}
