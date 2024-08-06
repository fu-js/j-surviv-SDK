package jsclub.codefest2024.sdk.socket.data.receive_data;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import jsclub.codefest2024.sdk.model.ElementType;


/**
 * inventory data to update when receive message from server
 * */
public class InventoryUpdateData {

    @SerializedName("type")
    public ElementType itemType;

    @SerializedName("id")
    public String id;

    @SerializedName("action")
    public String action;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
