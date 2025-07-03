package jsclub.codefest.sdk.model.weapon;

import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.ElementType;

public class Weapon extends Element {
    private int pickupPoints = 0;
    private int hitPoints = 0;
    private double cooldown = 0;
    private int damage = 0;
    private int[] range = new int[2];
    private int explodeRange = 0;
    private int speed = 0;
    private int useCount = 0;

    public Weapon(String id, ElementType type, int pickupPoints, int hitPoints, double cooldown, int damage, int[] range, int explodeRange, int speed, int useCount) {
        super(id);
        this.setType(type);
        this.pickupPoints = pickupPoints;
        this.hitPoints = hitPoints;
        this.cooldown = cooldown;
        this.damage = damage;
        this.range = range;
        this.explodeRange = explodeRange;
        this.speed = speed;
        this.useCount = useCount;
    }

    public int getPickupPoints() {
        return pickupPoints;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public double getCooldown() {
        return cooldown;
    }

    public int getDamage() {
        return damage;
    }

    public int[] getRange() {
        return range;
    }

    public int getExplodeRange() {
        return explodeRange;
    }

    public int getSpeed() {
        return speed;
    }

    public int getUseCount() {
        return useCount;
    }
}