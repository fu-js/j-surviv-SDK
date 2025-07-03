package jsclub.codefest.sdk.socket.data.emit_data;

import com.google.gson.annotations.SerializedName;

public class PlayerUseSpecialAction {
    @SerializedName("direction")
    private String direction;

    public PlayerUseSpecialAction(String direction) {
        this.direction = direction;
    }
}
