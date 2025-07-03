package jsclub.codefest.sdk.factory;

import jsclub.codefest.sdk.model.support_items.SupportItem;

import java.util.Map;

public class SupportItemFactory {
    private static final Map<String, SupportItem> supportItemMap = Map.of(
        "GOD_LEAF",         new SupportItem("GOD_LEAF", 1,   10, 5),
        "SPIRIT_TEAR",      new SupportItem("SPIRIT_TEAR", 1,   15, 15),
        "MERMAID_TAIL",     new SupportItem("MERMAID_TAIL", 1,   20, 20),
        "PHOENIX_FEATHERS", new SupportItem("PHOENIX_FEATHERS", 3,   40, 25),
        "UNICORN_BLOOD",    new SupportItem("UNICORN_BLOOD", 6,   80, 30),
        "ELIXIR",           new SupportItem("ELIXIR", 0,   5, 30),
        "MAGIC",            new SupportItem("MAGIC", 0,   0, 30),
        "ELIXIR_OF_LIFE",   new SupportItem("ELIXIR_OF_LIFE", 0, 100, 30),
        "COMPASS",          new SupportItem("COMPASS", 4,   0, 60)
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