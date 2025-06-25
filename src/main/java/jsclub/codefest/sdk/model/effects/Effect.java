package jsclub.codefest.sdk.model.effects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class Effect {
    @SerializedName("id")
    public String id;

    @SerializedName("duration")
    public Long duration;

    @SerializedName("level")
    public Integer level;

    public Effect() {
    }

    public Effect(Long duration, String id, Integer level) {
        this.duration = duration;
        this.id = id;
        this.level = level;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
