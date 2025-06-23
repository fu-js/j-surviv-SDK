package jsclub.codefest.sdk.model.players;

import com.google.gson.annotations.SerializedName;
import jsclub.codefest.sdk.factory.ArmorFactory;
import jsclub.codefest.sdk.factory.HealingItemFactory;
import jsclub.codefest.sdk.factory.WeaponFactory;
import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.Inventory;
import jsclub.codefest.sdk.model.effects.Effect;
import jsclub.codefest.sdk.socket.data.receive_data.ItemData;

import java.util.List;

public class Player extends Element {

    @SerializedName("health")
    private Float health;
    @SerializedName("score")
    private int score;
//    @SerializedName("inventory")
//    private List<ItemData> inventory;
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
//        return new Inventory(this.inventory);
//    }

}