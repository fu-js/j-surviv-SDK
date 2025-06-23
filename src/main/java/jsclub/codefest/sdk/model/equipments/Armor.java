package jsclub.codefest.sdk.model.equipments;

import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.effects.Effect;

import java.util.ArrayList;
import java.util.Arrays;

public class Armor extends Element {
    private final int damageReduce;
    private final double healthPoint;

    public Armor(String id, ElementType type, int rarity, double healthPoint, int damageReduce) {
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
