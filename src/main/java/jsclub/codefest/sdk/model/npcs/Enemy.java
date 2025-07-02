package jsclub.codefest.sdk.model.npcs;

import jsclub.codefest.sdk.model.ElementType;

public class Enemy extends NPC {
    private final int damage;

    public Enemy(String id, int damage) {
        super(id);
        this.damage = damage;
        this.setType(ElementType.ENEMY);
        // Default values
        this.setCooldown(6);
    }

    public int getDamage() {
        return damage;
    }
}
