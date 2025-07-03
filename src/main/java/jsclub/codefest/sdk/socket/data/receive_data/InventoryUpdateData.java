package jsclub.codefest.sdk.socket.data.receive_data;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import jsclub.codefest.sdk.model.ElementType;

public class InventoryUpdateData {
    @SerializedName("item_type")
    public ElementType itemType;

    @SerializedName("item")
    public String id;

    @SerializedName("action")
    public String action;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
