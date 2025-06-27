package jsclub.codefest.sdk.factory;

import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.armors.Armor;

import java.util.Map;


public class ArmorFactory {
    /**
     * Available Armors
     */
    private static final Map<String, Armor> armorMap = Map.of(
        "WOODEN_HELMET", new Armor("WOODEN_HELMET", ElementType.HELMET, 20, 5),
        "ARMOR", new Armor("ARMOR", ElementType.ARMOR, 50, 20),
        "MAGIC_HELMET", new Armor("MAGIC_HELMET", ElementType.HELMET, 50, 20),
            "MAGIC_ARMOR", new Armor ("MAGIC_ARMOR", ElementType.ARMOR , 75, 30)
    );
    /**
     * Find armor by id.
     *
     * @param id String to find armor.
     * @return Armor mapped with id.
     */
    public static Armor getArmorById(String id) {
        return armorMap.get(id);
    }

    /**
     * Find armor by id.
     * Set position for armor
     *
     * @param id String to find armor.
     * @param x,y int to set position.
     * @return Armor with updated position,id.
     * @throws CloneNotSupportedException If clone is not supported.
     */
    public static Armor getArmor(String id, int x, int y) throws CloneNotSupportedException {
        Armor armorBase = getArmorById(id);

        Armor armor = (Armor) armorBase.clone();
        armor.setPosition(x, y);
        armor.setId(id);
        return armor;
    }
}
