package jsclub.codefest.sdk.model.support_items;

import jsclub.codefest.sdk.model.Element;
import jsclub.codefest.sdk.model.ElementType;

public class SupportItem extends Element {
    private final int healingHP;
    private final int usageTime;
    private final int point;

    /**
     * Note: Server data will still send HEALING_ITEM but we use SUPPORT_ITEM as default type
     * since server code cannot be changed.
     */
    public SupportItem(String id, int usageTime, int healingHP, int point) {
        super(id);
        this.healingHP = healingHP;
        this.usageTime = usageTime;
        this.point = point;

        this.setType(ElementType.SUPPORT_ITEM);  // Default type set to SUPPORT_ITEM
    }

    public int getHealingHP() {
        return healingHP;
    }

    public int getUsageTime() {
        return usageTime;
    }

    public int getPoint() {
        return point;
    }
}