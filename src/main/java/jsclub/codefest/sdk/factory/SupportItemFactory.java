package jsclub.codefest.sdk.factory;

import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.support_items.SupportItem;

import java.util.List;
import java.util.Map;

public class SupportItemFactory {
    /**
     * Available SupportItems
     * Note: All SupportItems now use SUPPORT_ITEM type by default. 
     * Server data may still send HEALING_ITEM but SupportItem class handles this internally.
     */
    private static final Map<String, SupportItem> supportItemMap = Map.of(
        "GOD_LEAF", new SupportItem("GOD_LEAF", 0.5, 10, 0, 5, null),
        "SPIRIT_TEAR", new SupportItem("SPIRIT_TEAR", 0.5, 15, 0, 15, null),
        "MERMAID_TAIL", new SupportItem("MERMAID_TAIL", 1, 20, 0, 20, null),
        "PHOENIX_FEATHERS", new SupportItem("PHOENIX_FEATHERS", 1.5, 40, 0, 25, null),
        "UNICORN_BLOOD", new SupportItem("UNICORN_BLOOD", 3, 80, 0, 30, null),
        "ELIXIR", new SupportItem("ELIXIR", 0, 5, 7, 30, List.of(EffectFactory.getEffects("CONTROL_IMMUNITY")) ),
        "MAGIC", new SupportItem("MAGIC", 0, 0, 5, 30, List.of(EffectFactory.getEffects("INVISIBLE")) ),
        "ELIXIR_OF_LIFE", new SupportItem("ELIXIR_OF_LIFE", 0, 100, 0, 30, List.of(EffectFactory.getEffects("REVIVAL"), EffectFactory.getEffects("UNDEAD")) ),
        "COMPASS", new SupportItem("COMPASS", 2, 0, 7, 60, List.of(EffectFactory.getEffects("STUN")) )
    );

    /**
     * Find support item by id.
     *
     * @param id String to find support item.
     * @return SupportItem mapped with id.
     */
    public static SupportItem getSupportItemById(String id) {
        return supportItemMap.get(id);
    }

    /**
     * Find support item by id.
     * Set position for support item
     *
     * @param id String to find support item.
     * @param x,y int to set position.
     * @return SupportItem with updated position,id.
     * @throws CloneNotSupportedException If clone is not supported.
     */
    public static SupportItem getSupportItem(String id, int x, int y) throws CloneNotSupportedException {
        SupportItem supportItemBase = getSupportItemById(id);

        SupportItem supportItem = (SupportItem) supportItemBase.clone();
        supportItem.setPosition(x, y);
        supportItem.setId(id);
        return supportItem;
    }
} 