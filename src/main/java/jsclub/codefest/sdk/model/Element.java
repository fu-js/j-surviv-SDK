package jsclub.codefest.sdk.model;

import com.google.gson.Gson;
import jsclub.codefest.sdk.base.Node;

public class Element extends Node implements Cloneable {
    private String id;
    private ElementType type = ElementType.ROAD;

    public Element(String id) {
        this.id = id;
    }

    public Element(int x, int y, String id, ElementType type) {
        super(x, y);
        this.id = id;
        this.type = type;
    }

    public Element() {}

    public String getId() {
        return id;
    }
    public ElementType getType() {
        return type;
    }

    public Node getPosition() {
        return new Node(this.x, this.y);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
