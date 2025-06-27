package jsclub.codefest.sdk.factory;

import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.healing_items.HealingItem;

import java.util.List;
import java.util.Map;

public class HealingItemFactory {
    /**
     * Available HealingItems
     */
    private static final Map<String, HealingItem> healingItemMap = Map.of(
        "GOD_LEAF", new HealingItem("GOD_LEAF", ElementType.HEALING_ITEM, 0.5, 10, 0, 5, null),
        "SPIRIT_TEAR", new HealingItem("SPIRIT_TEAR", ElementType.HEALING_ITEM, 0.5, 15, 0, 15, null),
        "MERMAID_TAIL", new HealingItem("MERMAID_TAIL", ElementType.HEALING_ITEM, 1, 20, 0, 20, null),
        "PHOENIX_FEATHERS", new HealingItem("PHOENIX_FEATHERS", ElementType.HEALING_ITEM, 1.5, 40, 0, 25, null),
        "UNICORN_BLOOD", new HealingItem("UNICORN_BLOOD", ElementType.HEALING_ITEM, 3, 80, 0, 30, null),
            "ELIXIR", new HealingItem("ELIXIR", ElementType.SPECIAL, 0, 5, 7, 30, List.of(EffectFactory.getEffects("CONTROL_IMMUNITY")) ),
            "MAGIC", new HealingItem("MAGIC", ElementType.SPECIAL, 0, 0, 5, 30, List.of(EffectFactory.getEffects("INVISIBLE")) ),
            "ELIXIR_OF_LIFE", new HealingItem("ELIXIR_OF_LIFE", ElementType.SPECIAL, 0, 100, 0, 30, List.of(EffectFactory.getEffects("REVIVAL"), EffectFactory.getEffects("UNDEAD")) ),
            "COMPASS", new HealingItem("COMPASS", ElementType.SPECIAL, 2, 0, 7, 60, List.of(EffectFactory.getEffects("STUN")) )


    );

    /**
     * Find healing item by id.
     *
     * @param id String to find healing item.
     * @return HealingItem mapped with id.
     */
    public static HealingItem getHealingItemById(String id) {
        return healingItemMap.get(id);
    }

    /**
     * Find healing item by id.
     * Set position for healing item
     *
     * @param id String to find healing item.
     * @param x,y int to set position.
     * @return HealingItem with updated position,id.
     * @throws CloneNotSupportedException If clone is not supported.
     */
    public static HealingItem getHealingItem(String id, int x, int y) throws CloneNotSupportedException {
        HealingItem healingItemBase = getHealingItemById(id);

        HealingItem healingItem = (HealingItem) healingItemBase.clone();
        healingItem.setPosition(x, y);
        healingItem.setId(id);
        return healingItem;
    }
}
