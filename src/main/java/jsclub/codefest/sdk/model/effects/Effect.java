package jsclub.codefest.sdk.model.effects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Effect {
    @SerializedName("id")
    public String id;

    @SerializedName("duration")
    public Integer duration;

    public Effect() {
    }

    public Effect(Integer duration, String id) {
        this.duration = duration;
        this.id = id;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
