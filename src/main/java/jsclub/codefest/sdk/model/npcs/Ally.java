package jsclub.codefest.sdk.model.npcs;

import jsclub.codefest.sdk.model.ElementType;

public class Ally extends NPC {
    private final int healingHP;

    public Ally(String id, int healingHP) {
        super(id);
        this.healingHP = healingHP;
        this.setType(ElementType.ALLY);

        // Default values
        this.setCooldown(20);
    }

    public int getHealingHP() {
        return healingHP;
    }
}
