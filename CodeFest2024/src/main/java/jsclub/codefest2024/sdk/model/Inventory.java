package jsclub.codefest2024.sdk.model;

import jsclub.codefest2024.sdk.factory.WeaponFactory;
import jsclub.codefest2024.sdk.model.equipments.Armor;
import jsclub.codefest2024.sdk.model.equipments.HealingItem;
import jsclub.codefest2024.sdk.model.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private Weapon gun;
    private Weapon melee;
    private List<Weapon> listThrowable = new ArrayList<>();
    private Armor armor;
    private List<HealingItem> listHealingItem = new ArrayList<>();

   /**
   * constructor
   * */
    public Inventory() {
        // Set default value for melee is HAND
        this.melee = WeaponFactory.getWeaponById("HAND");
    }

    /**
    * getter and setter
    * */
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

    public List<Weapon> getListThrowable() {
        return listThrowable;
    }

    public void setListThrowable(List<Weapon> listThrowable) {
        this.listThrowable = listThrowable;
    }

    public List<HealingItem> getListHealingItem() {
        return listHealingItem;
    }

    public void setListHealingItem(List<HealingItem> listHealingItem) {
        this.listHealingItem = listHealingItem;
    }

    public Armor getArmor() {
        return armor;
    }

    public void setArmor(Armor armor) {
        this.armor = armor;
    }
}
