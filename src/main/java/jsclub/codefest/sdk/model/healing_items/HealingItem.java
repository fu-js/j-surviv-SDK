package jsclub.codefest.sdk.model.healing_items;

import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.effects.Effect;
import java.util.List;

public class HealingItem extends Element {
    private int duration;

    private final int healingHP;
    private final double usageTime;
    private final int point;
    private List<Effect>  effects;


    public HealingItem(String id, ElementType type, double usageTime, int healingHp, int duration, int point, List<Effect> effects) {
        super(id);
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

    public List<Effect>  getEffects() {
        return effects;
    }

    public int getPoint() {
        return point;
    }
}
