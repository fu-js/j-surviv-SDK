package jsclub.codefest.sdk.socket.data.emit_data;

import com.google.gson.annotations.SerializedName;

public class PlayerThrowItemAction {
    @SerializedName("direction")
    private String direction;

    // @SerializedName("distance")
    // private int distance;

    public PlayerThrowItemAction(String direction) {
        this.direction = direction;
        // this.distance = distance;
    }
}
