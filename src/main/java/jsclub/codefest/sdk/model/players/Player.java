package jsclub.codefest.sdk.model.players;

import com.google.gson.annotations.SerializedName;
import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.Inventory;
import jsclub.codefest.sdk.model.effects.Effect;
import jsclub.codefest.sdk.socket.data.receive_data.ItemData;

import java.util.List;

public class Player extends Element {
//    @SerializedName("player_name")
//    private String playerName;
//
//    @SerializedName("bullet_num")
//    private int bulletNum;
//
//    @SerializedName("damage_reduction")
//    private int damageReduction;
//
//    @SerializedName("is_alive")
//    private boolean isAlive;

//    @SerializedName("id")
//    private String ID;

    @SerializedName("health")
    private Float health;
    @SerializedName("score")
    private int score;
    @SerializedName("inventory")
    private List<ItemData> inventory;
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

//    public Inventory getInventory() {
//        return inventory;
//    }

    public Inventory getInventory(){
        return new Inventory(inventory);
    }

    public List<String> getCanBeSeenBy() {
        return canBeSeenBy;
    }

    public List<Effect> getEffects() {
        return effects;
    }
}
