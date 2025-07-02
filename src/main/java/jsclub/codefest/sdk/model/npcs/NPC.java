package jsclub.codefest.sdk.model.npcs;

import jsclub.codefest.sdk.model.Element;

public abstract class NPC extends Element {
    private final int speed;          // cell/step
    private final int attackRange;    // cell
    private int cooldown;       // step

    public NPC(String id, int speed, int attackRange, int cooldown) {
        super(id);
        this.speed = speed;
        this.attackRange = attackRange;
        this.cooldown = cooldown;
    }

	public NPC(String id) {
		super(id);
		// Set default values
		this.speed = 1;
		this.attackRange = 3;
        this.cooldown = 1;
	}

    public int getSpeed() {
        return speed;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        if (cooldown < 0) {
            throw new IllegalArgumentException("Cooldown cannot be negative");
        }
        this.cooldown = cooldown;
    }
} 