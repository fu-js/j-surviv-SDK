package jsclub.codefest.sdk.model.players;

import com.google.gson.annotations.SerializedName;
import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.effects.Effect;

import java.util.List;

public class Player extends Element {
    @SerializedName("health")
    private Float health;
    @SerializedName("score")
    private int score;
    @SerializedName("canBeSeenBy")
    private List<String> canBeSeenBy;
    @SerializedName("effects")
    private List<Effect> effects;

    public Player() {
        setType(ElementType.PLAYER);
    }

    public String getID() {
        return super.getId();
    }

    public Float getHealth() {
        return health;
    }

    public int getScore() {
        return score;
    }
}