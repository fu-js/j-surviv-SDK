package jsclub.codefest.sdk.socket.data.receive_data;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import jsclub.codefest.sdk.model.ElementType;

public class ItemData {
    @SerializedName("item")
    public Item item;

    public String getId() {
        return item.ID;
    }

    public ElementType getType() {
        return item.type;
    }

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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
