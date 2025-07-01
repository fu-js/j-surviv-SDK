package jsclub.codefest.sdk.factory;

import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.weapon.Weapon;

import java.util.List;
import java.util.Map;
import jsclub.codefest.sdk.model.weapon.AttackRange;
import jsclub.codefest.sdk.model.weapon.Bullet;

public class WeaponFactory {
    /**
     * Available Weapons
     */
    private static final Map<String, Weapon> weaponMap = Map.ofEntries(
            Map.entry("KNIFE", new Weapon("KNIFE", ElementType.MELEE, 35, 30, 3, 0, 30, 3, 0, AttackRange.SHORT, new Bullet(0, 0), null)),
            Map.entry("TREE_BRANCH", new Weapon("TREE_BRANCH", ElementType.MELEE, 45, 15, 2, 0, 15, 3, 0, AttackRange.SHORT, new Bullet(0, 0), null)),
            Map.entry("HAND", new Weapon("HAND", ElementType.MELEE, 0, 5, 1, 0, 5, 1, 0, AttackRange.SHORT, new Bullet(0, 0), null)),
            Map.entry("BONE", new Weapon("BONE", ElementType.MELEE, 40, 30, 4, 0, 30, 1, 0, AttackRange.SHORT, new Bullet(0, 0), null)),
            Map.entry("AXE", new Weapon("AXE", ElementType.MELEE, 30, 40, 5, 0, 40, 3, 0, AttackRange.SHORT, new Bullet(0, 0), null)),
            Map.entry("SCEPTER", new Weapon("SCEPTER", ElementType.GUN, 30, 20, 3, 10, 20, 12, 0, AttackRange.LONG, new Bullet(20, 6), null)),
            Map.entry("SMOKE", new Weapon("SMOKE", ElementType.THROWABLE, 50, 0, 0, 1, 0, 3, 49, AttackRange.MID, new Bullet(0, 6), List.of(EffectFactory.getEffects("BLIND"), EffectFactory.getEffects("INVISIBLE")) )),
            Map.entry("CROSSBOW", new Weapon("CROSSBOW", ElementType.GUN, 30, 30, 4, 6, 30, 8, 0, AttackRange.LONG, new Bullet(30, 4), null)),
            Map.entry("RUBBER_GUN", new Weapon("RUBBER_GUN", ElementType.GUN, 40, 15, 2, 8, 15, 6, 0, AttackRange.LONG, new Bullet(15, 4), null)),
            Map.entry("SHOTGUN", new Weapon("SHOTGUN", ElementType.GUN, 20, 45, 8, 4, 45, 2, 0, AttackRange.LONG, new Bullet(50, 8), null)),
            Map.entry("METEORITE_FRAGMENT", new Weapon("METEORITE_FRAGMENT", ElementType.THROWABLE, 35, 35, 4, 2, 35, 6, 9, AttackRange.LONG, new Bullet(35, 6), null)),
            Map.entry("CRYSTAL", new Weapon("CRYSTAL", ElementType.THROWABLE, 30, 45, 4, 2, 45, 6, 9, AttackRange.LONG, new Bullet(45, 6), null)),
            Map.entry("BANANA", new Weapon("BANANA", ElementType.THROWABLE, 35, 30, 4, 2, 30, 6, 9, AttackRange.LONG, new Bullet(30, 6), null)),
            Map.entry("SEED", new Weapon("SEED", ElementType.THROWABLE, 50, 20, 8, 2, 20, 5, 9, AttackRange.LONG, new Bullet(20, 6), List.of(EffectFactory.getEffects("STUN")) )),
            Map.entry("MACE", new Weapon("MACE", ElementType.MELEE, 60, 60, 10, 2, 60, 9, 0, AttackRange.MID, new Bullet(0, 0), List.of(EffectFactory.getEffects("STUN")))),
            Map.entry("ROPE", new Weapon("ROPE", ElementType.SPECIAL, 50, 15, 20, 2, 15, 6, 0, AttackRange.LONG, new Bullet(15, 12), List.of(EffectFactory.getEffects("STUN"), EffectFactory.getEffects("PULL")))),
            Map.entry("BELL", new Weapon("BELL", ElementType.SPECIAL, 60, 15, 0, 1, 15, 49, 0, AttackRange.LONG, new Bullet(0, 0), List.of(EffectFactory.getEffects("REVERSE")))),
            Map.entry("SAHUR_BAT", new Weapon("SAHUR_BAT", ElementType.SPECIAL, 50, 20, 20, 3, 20, 5, 0, AttackRange.LONG, new Bullet(20, 5), List.of(EffectFactory.getEffects("KNOCKBACK"))))
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
