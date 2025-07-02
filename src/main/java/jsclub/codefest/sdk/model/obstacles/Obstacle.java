package jsclub.codefest.sdk.model.obstacles;

import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.ElementType;

import java.util.ArrayList;
import java.util.List;

public class Obstacle extends Element {
    private int hp = -1;
    private List<ObstacleTag> tags = new ArrayList<>();

    public Obstacle(String id, ElementType type, List<ObstacleTag> tags , int hp) {
        super(id);
        this.tags = tags;
        this.hp = hp;
        this.setType(type);
    }

    public Obstacle(String id, ElementType type, List<ObstacleTag> tags) {
        super(id);
        this.tags = tags;
        this.setType(type);
    }

    public int getHp() {
        return hp;
    }

    public List<ObstacleTag> getTags() {
        return tags;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    // public void setTags(List<ObstacleTag> tags) {
    //     this.tags = tags;
    // }
}
