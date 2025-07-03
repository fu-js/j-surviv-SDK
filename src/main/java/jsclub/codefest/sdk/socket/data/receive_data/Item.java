package jsclub.codefest.sdk.socket.data.receive_data;

import com.google.gson.annotations.SerializedName;
import jsclub.codefest.sdk.model.ElementType;

public class Item {
    @SerializedName("id")
    public String ID;

    @SerializedName("type")
    public ElementType type;

    @SerializedName("level")
    public int level;

    @SerializedName("durability")
    public Long durability;

    @Override
    public String toString() {
        return "id:"+this.ID + ", type:"+ this.type;
    }
}