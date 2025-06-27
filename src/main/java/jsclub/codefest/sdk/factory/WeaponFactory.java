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
            Map.entry("KNIFE", new Weapon("KNIFE", ElementType.MELEE, 55, 25, 2.5, 0, 25, 3, 0, AttackRange.SHORT, new Bullet(0, 0), null)),
            Map.entry("TREE_BRANCH", new Weapon("TREE_BRANCH", ElementType.MELEE, 35, 10, 0.5, 0, 10, 3, 0, AttackRange.SHORT, new Bullet(0, 0), null)),
            Map.entry("HAND", new Weapon("HAND", ElementType.MELEE, 0, 5, 0.5, 0, 5, 1, 0, AttackRange.SHORT, new Bullet(0, 0), null)),
            Map.entry("BONE", new Weapon("BONE", ElementType.MELEE, 45, 20, 2, 0, 20, 1, 0, AttackRange.SHORT, new Bullet(0, 0), null)),
            Map.entry("AXE", new Weapon("AXE", ElementType.MELEE, 45, 45, 3, 0, 45, 3, 0, AttackRange.SHORT, new Bullet(0, 0), null)),
            Map.entry("SCEPTER", new Weapon("SCEPTER", ElementType.GUN, 40, 20, 1, 10, 0, 12, 0, AttackRange.LONG, new Bullet(20, 6), null)),
            Map.entry("SMOKE", new Weapon("SMOKE", ElementType.THROWABLE, 50, 0, 10, 2, 0, 3, 49, AttackRange.MID, new Bullet(0, 6), List.of(EffectFactory.getEffects("BLIND"), EffectFactory.getEffects("INVISIBLE")) )),
            Map.entry("CROSSBOW", new Weapon("CROSSBOW", ElementType.GUN, 35, 25, 1.5, 5, 25, 4, 0, AttackRange.LONG, new Bullet(25, 4), null)),
            Map.entry("RUBBER_GUN", new Weapon("RUBBER_GUN", ElementType.GUN, 50, 10, 1, 10, 10, 12, 0, AttackRange.LONG, new Bullet(10, 4), null)),
            Map.entry("SHOTGUN", new Weapon("SHOTGUN", ElementType.GUN, 20, 60, 4, 5, 60, 2, 0, AttackRange.LONG, new Bullet(40, 8), null)),
            Map.entry("METEORITE_FRAGMENT", new Weapon("METEORITE_FRAGMENT", ElementType.THROWABLE, 15, 60, 2, 0, 60, 6, 9, AttackRange.LONG, new Bullet(60, 6), null)),
            Map.entry("CRYSTAL", new Weapon("CRYSTAL", ElementType.THROWABLE, 30, 60, 2, 0, 60, 6, 9, AttackRange.LONG, new Bullet(60, 6), null)),
            Map.entry("BANANA", new Weapon("BANANA", ElementType.THROWABLE, 30, 30, 2, 2, 30, 6, 9, AttackRange.LONG, new Bullet(30, 6), null)),
            Map.entry("SEED", new Weapon("SEED", ElementType.THROWABLE, 45, 30, 10, 2, 30, 5, 9, AttackRange.LONG, new Bullet(30, 6), List.of(EffectFactory.getEffects("STUN")) )),
            Map.entry("MACE", new Weapon("MACE", ElementType.MELEE, 60, 40, 5, 2, 40, 9, 0, AttackRange.MID, new Bullet(0, 0), List.of(EffectFactory.getEffects("STUN")))),
            Map.entry("ROPE", new Weapon("ROPE", ElementType.SPECIAL, 80, 5, 10, 2, 5, 6, 0, AttackRange.LONG, new Bullet(0, 12), List.of(EffectFactory.getEffects("STUN"), EffectFactory.getEffects("PULL")))),
            Map.entry("BELL", new Weapon("BELL", ElementType.SPECIAL, 70, 15, 0, 1, 15, 49, 0, AttackRange.LONG, new Bullet(0, 0), List.of(EffectFactory.getEffects("REVERSE")))),
            Map.entry("SAHUR_BAT", new Weapon("SAHUR_BAT", ElementType.SPECIAL, 70, 20, 10, 3, 20, 5, 0, AttackRange.LONG, new Bullet(20, 5), List.of(EffectFactory.getEffects("KNOCKBACK"))))
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
