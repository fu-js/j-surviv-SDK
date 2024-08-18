package jsclub.codefest2024.sdk.socket.data.emit_data;

import com.google.gson.annotations.SerializedName;

public class PlayerThrowItemAction {
    @SerializedName("direction")
    private String direction;

    public PlayerThrowItemAction(String direction) {
        this.direction = direction;
    }
}
