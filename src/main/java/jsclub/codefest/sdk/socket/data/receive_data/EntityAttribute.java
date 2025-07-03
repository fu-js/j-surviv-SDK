package jsclub.codefest.sdk.socket.data.receive_data;

import com.google.gson.annotations.SerializedName;

public class EntityAttribute {
    @SerializedName("speed")
    public int speed;

    @SerializedName("damage")
    public float damage;

    @SerializedName("destination_x")
    public int destinationX;

    @SerializedName("destination_y")
    public int destinationY;

    @SerializedName("is_cooldown")
    public boolean isCooldownActive;

    @SerializedName("cooldown_step_left")
    public int cooldownStepLeft;

    @SerializedName("current_hp")
    public int currentHp;
}
