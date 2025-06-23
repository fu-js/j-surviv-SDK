package jsclub.codefest.sdk.socket.data.receive_data;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.obstacles.ObstacleTag;

import java.util.List;

public class Block {
    @SerializedName("x")
    public int x;

    @SerializedName("y")
    public int y;
    
    @SerializedName("id")
    public String id;
    
    @SerializedName("type")
    public ElementType type;
    
    @SerializedName("tags")
    public List<ObstacleTag> tags;

//    @SerializedName("attributes")
//    public List<Object> attributes;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
