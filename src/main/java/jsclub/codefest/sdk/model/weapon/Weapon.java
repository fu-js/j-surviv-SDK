package jsclub.codefest.sdk.model.weapon;

import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.effects.Effect;

import java.util.List;

public class Weapon extends Element {
    private int pickupPoints = 0;
    private int hitPoints = 0;
    private double cooldown = 0;
    private int useCounts = 0;
    private int damage = 0;
    private int range = 0;
    private int explosionRange = 0;
    private AttackRange attackRange = null;
    private Bullet bullet = new Bullet();
    private List<Effect> effects;


    public Weapon(String id, ElementType type, int pickupPoints, int hitPoints, double cooldown, int useCounts, int damage, int range, int explosionRange, AttackRange attackRange, Bullet bullet, List<Effect> effects) {
        super(id);
        this.pickupPoints = pickupPoints;
        this.hitPoints = hitPoints;
        this.cooldown = cooldown;
        this.useCounts = useCounts;
        this.damage = damage;
        this.range = range;
        this.explosionRange = explosionRange;
        this.attackRange = attackRange;
        this.bullet = bullet;

        this.setType(type);
        this.effects = effects;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void setEffects(List<Effect> effects) {
        this.effects = effects;
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

    public int getUseCounts() {
        return useCounts;
    }

    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public int getExplosionRange() {
        return explosionRange;
    }

    public AttackRange getAttackRange() {
        return attackRange;
    }

    public Bullet getBullet() {
        return bullet;
    }
}