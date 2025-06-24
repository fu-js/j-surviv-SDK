package jsclub.codefest.sdk.socket.data.emit_data;

import com.google.gson.annotations.SerializedName;
import jsclub.codefest.sdk.model.weapon.Weapon;

public class PlayerUseSpecialAction {
    @SerializedName("direction")
    private String direction;

    public PlayerUseSpecialAction(String direction) {
        this.direction = direction;
    }
}
