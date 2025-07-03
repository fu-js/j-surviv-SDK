package jsclub.codefest.sdk.factory;

import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.weapon.Weapon;

import java.util.Map;

public class WeaponFactory {
    /**
     * Available Weapons
     */
    private static final Map<String, Weapon> weaponMap = Map.ofEntries(
            // Melee Weapons
            Map.entry("KNIFE", new Weapon("KNIFE", ElementType.MELEE, 35, 30, 3, 30, new int[]{3, 1}, -1, -1, -1)),
            Map.entry("TREE_BRANCH", new Weapon("TREE_BRANCH", ElementType.MELEE, 45, 15, 2, 15, new int[]{3, 1}, -1, -1, -1)),
            Map.entry("HAND", new Weapon("HAND", ElementType.MELEE, 0, 5, 1, 5, new int[]{1, 1}, -1, -1, -1)),
            Map.entry("BONE", new Weapon("BONE", ElementType.MELEE, 40, 30, 4, 30, new int[]{1, 1}, -1, -1, -1)),
            Map.entry("AXE", new Weapon("AXE", ElementType.MELEE, 30, 40, 5, 40, new int[]{3, 1}, -1, -1, -1)),
            Map.entry("MACE", new Weapon("MACE", ElementType.MELEE, 60, 60, 10, 60, new int[]{3, 3}, -1, -1, 2)),
            
            // Gun Weapons
            Map.entry("SCEPTER", new Weapon("SCEPTER", ElementType.GUN, 30, 20, 3, 20, new int[]{1, 12}, -1, 12, 10)),
            Map.entry("CROSSBOW", new Weapon("CROSSBOW", ElementType.GUN, 30, 30, 4, 30, new int[]{1, 8}, -1, 8, 6)),
            Map.entry("RUBBER_GUN", new Weapon("RUBBER_GUN", ElementType.GUN, 40, 15, 2, 15, new int[]{1, 6}, -1, 8, 8)),
            Map.entry("SHOTGUN", new Weapon("SHOTGUN", ElementType.GUN, 20, 45, 8, 45, new int[]{1, 2}, -1, 16, 4)),
            
            // Throwable Weapons
            Map.entry("BANANA", new Weapon("BANANA", ElementType.THROWABLE, 35, 30, 4, 30, new int[]{1, 6}, 3, 3, 2)),
            Map.entry("SMOKE", new Weapon("SMOKE", ElementType.THROWABLE, 50, 0, 0, 0, new int[]{1, 3}, 7, 3, 1)),
            Map.entry("METEORITE_FRAGMENT", new Weapon("METEORITE_FRAGMENT", ElementType.THROWABLE, 35, 35, 4, 35, new int[]{1, 6}, 3, 3, 2)),
            Map.entry("CRYSTAL", new Weapon("CRYSTAL", ElementType.THROWABLE, 30, 45, 4, 45, new int[]{1, 6}, 3, 3, 2)),
            Map.entry("SEED", new Weapon("SEED", ElementType.THROWABLE, 50, 20, 8, 20, new int[]{1, 5}, 3, 3, 2)),
            
            // Special Weapons
            Map.entry("ROPE", new Weapon("ROPE", ElementType.SPECIAL, 50, 15, 20, 15, new int[]{1, 6}, -1, -1, 2)),
            Map.entry("BELL", new Weapon("BELL", ElementType.SPECIAL, 60, 15, 0, 15, new int[]{7, 7}, -1, -1, 1)),
            Map.entry("SAHUR_BAT", new Weapon("SAHUR_BAT", ElementType.SPECIAL, 50, 20, 20, 20, new int[]{1, 5}, -1, -1, 3))
    );

    /**
     * Find weapon by id.
     *
     * @param id String to find weapon.
     * @return Weapon mapped with id.
     */
    public static Weapon getWeaponById(String id) {
        return weaponMap.get(id);
    }

    /**
     * Find weapon by id.
     * Set position for weapon
     *
     * @param id  String to find weapon.
     * @param x,y int to set position.
     * @return Weapon with updated position,id.
     * @throws CloneNotSupportedException If clone is not supported.
     */
    public static Weapon getWeapon(String id, int x, int y) throws CloneNotSupportedException {
        Weapon weaponBase = weaponMap.get(id);
        Weapon weapon = (Weapon) weaponBase.clone();
        weapon.setPosition(x, y);
        weapon.setId(id);
        return weapon;
    }
}
