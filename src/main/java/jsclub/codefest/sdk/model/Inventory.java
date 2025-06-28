package jsclub.codefest.sdk.model;

import com.google.gson.Gson;
import jsclub.codefest.sdk.factory.WeaponFactory;
import jsclub.codefest.sdk.model.armors.Armor;
import jsclub.codefest.sdk.model.healing_items.HealingItem;
import jsclub.codefest.sdk.model.weapon.Weapon;
import java.util.ArrayList;
import java.util.List;

public class Inventory
{
    private Weapon gun;
    private Weapon melee;
    private Weapon throwable;
    private Weapon special;
    private Armor armor;
    private Armor helmet;
    private List<HealingItem> listHealingItem = new ArrayList<>();

    public Inventory() {
        // Set default value for melee is HAND
        this.melee = WeaponFactory.getWeaponById("HAND");
    }

    public Weapon getGun() {
        return gun;
    }

    public void setGun(Weapon gun) {
        this.gun = gun;
    }

    public Weapon getMelee() {
        return melee;
    }

    public void setMelee(Weapon melee) {
        this.melee = melee;
    }

    public Weapon getThrowable() {
        return throwable;
    }

    public void setThrowable(Weapon throwable) {
        this.throwable = throwable;
    }

    public Weapon getSpecial() {
        return special;
    }

    public void setSpecial(Weapon special) {
        this.special = special;
    }

    public Armor getHelmet() {
        return helmet;
    }

    public void setHelmet(Armor helmet) {
        this.helmet = helmet;
    }

    public Armor getArmor() {
        return armor;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }

    public List<HealingItem> getListHealingItem() {
        return listHealingItem;
    }

    public void setListHealingItem(List<HealingItem> listHealingItem) {
        this.listHealingItem = listHealingItem;
    }

    public void reset() {
        this.setGun(null);
        this.setMelee(WeaponFactory.getWeaponById("HAND"));
        this.setThrowable(null);
        this.setSpecial(null);
        this.setArmor(null);
        this.setHelmet(null);
        this.setListHealingItem(new ArrayList<>());

    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

