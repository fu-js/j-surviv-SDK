package jsclub.codefest.sdk.model.effects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Effect {
    @SerializedName("id")
    public String id;

    @SerializedName("duration")
    public int duration;

    @SerializedName("affected_at")
    public int affectedAt;

    @SerializedName("estimated_end_at")
    public int estimatedEndAt;

    public Effect(Integer duration, String id) {
        this.duration = duration;
        this.id = id;
    }

    public Effect(Integer duration, String id, int affectedAt, int estimatedEndAt) {
        this.duration = duration;
        this.id = id;
        this.affectedAt = affectedAt;
        this.estimatedEndAt = estimatedEndAt;
    }

    public void setAffectedAt(int affectedAt) {
        this.affectedAt = affectedAt;
    }

    public void setEstimatedEndAt(int estimatedEndAt) {
        this.estimatedEndAt = estimatedEndAt;
    }

    public int getDuration() {
        return duration;
    }

    public int getAffectedAt() {
        return affectedAt;
    }

    public int getEstimatedEndAt() {
        return estimatedEndAt;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
