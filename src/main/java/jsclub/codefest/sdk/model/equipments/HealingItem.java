package jsclub.codefest.sdk.model.equipments;

import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.effects.Effect;
import java.util.List;

public class HealingItem extends Element {
    private int rarity;
    private int duration;

    private final int healingHP;
    private final double usageTime;
    private final int point;
    private List<Effect>  effects;


    public HealingItem(String id, ElementType type, int rarity, double usageTime, int healingHp, int duration, int point, List<Effect> effects) {
        super(id);
        this.rarity = rarity;
        this.duration = duration;
        this.healingHP = healingHp;
        this.usageTime = usageTime;
        this.point = point;

        this.setType(type);
        this.effects = effects;
    }

    public int getHealingHP() {
        return healingHP;
    }

    public double getUsageTime() {
        return usageTime;
    }

    public int getDuration() {
        return duration;
    }

    public int getRarity() {
        return rarity;
    }

    public List<Effect>  getEffects() {
        return effects;
    }

    public int getPoint() {
        return point;
    }
}
