package jsclub.codefest.sdk.model.npcs;

import jsclub.codefest.sdk.model.Element;

public abstract class NPC extends Element {
    private final int speed;
    private final int attackRange;
    private int cooldown;

    private boolean isCooldownActive = false;
    private int cooldownStepLeft;

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

    public boolean isCooldownActive() {
        return isCooldownActive;
    }

    public int getCooldownStepLeft() {
        return cooldownStepLeft;
    }

    public void setCooldownActive(boolean isCooldownActive) {
        this.isCooldownActive = isCooldownActive;
    }

    public void setCooldownStepLeft(int cooldownStepLeft) {
        if (cooldownStepLeft < 0) {
            throw new IllegalArgumentException("Cooldown steps left cannot be negative");
        }
        this.cooldownStepLeft = cooldownStepLeft;
    }

    public void setCooldown(int cooldown) {
        if (cooldown < 0) {
            throw new IllegalArgumentException("Cooldown cannot be negative");
        }
        this.cooldown = cooldown;
    }
} 