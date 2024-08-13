package jsclub.codefest2024.sdk.socket.data.emit_data;

import com.google.gson.annotations.SerializedName;

public class PlayerShootAction {
    @SerializedName("direction")
    private String direction;

    public PlayerShootAction(String direction) {
        this.direction = direction;
    }
}
