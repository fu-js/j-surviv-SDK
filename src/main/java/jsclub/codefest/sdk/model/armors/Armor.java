package jsclub.codefest.sdk.model.armors;

import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.ElementType;

public class Armor extends Element {
    private final int damageReduce;
    private final double healthPoint;

    public Armor(String id, ElementType type, double healthPoint, int damageReduce) {
        super(id);
        this.damageReduce = damageReduce;
        this.healthPoint = healthPoint;
        this.setType(type);
    }

    public double getHealthPoint() {
        return healthPoint;
    }

    public int getDamageReduce() {
        return damageReduce;
    }
}
