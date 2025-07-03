package jsclub.codefest.sdk.factory;

import jsclub.codefest.sdk.model.npcs.Ally;

import java.util.Map;

public class AllyFactory {
    private static final Map<String, Ally> allyMap = Map.of(
        "SPIRIT", new Ally("SPIRIT", 50)
    );

    /**
     * Find ally by id.
     *
     * @param id String to find ally.
     * @return Ally mapped with id.
     */
    public static Ally getAllyById(String id) {
        return allyMap.get(id);
    }

    /**
     * Find ally by id.
     * Set position for ally
     *
     * @param id String to find ally.
     * @param x,y int to set position.
     * @return Ally with updated position,id.
     * @throws CloneNotSupportedException If clone is not supported.
     */
    public static Ally getAlly(String id, int x, int y) throws CloneNotSupportedException {
        Ally allyBase = getAllyById(id);

        Ally ally = (Ally) allyBase.clone();
        ally.setPosition(x, y);
        ally.setId(id);

        return ally;
    }

    public static Ally getAlly(String id, int x, int y, boolean isCooldownActive, int cooldownStepLeft) throws CloneNotSupportedException {
        Ally allyBase = getAlly(id, x, y);
        allyBase.setCooldownActive(isCooldownActive);
        allyBase.setCooldownStepLeft(cooldownStepLeft);

        return allyBase;
    }
}
