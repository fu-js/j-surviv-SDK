package jsclub.codefest.sdk.socket.event_handler;

import com.google.gson.Gson;
import io.socket.emitter.Emitter;
import jsclub.codefest.sdk.factory.ArmorFactory;
import jsclub.codefest.sdk.factory.SupportItemFactory;
import jsclub.codefest.sdk.factory.WeaponFactory;
import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.Inventory;
import jsclub.codefest.sdk.socket.data.receive_data.InventoryUpdateData;
import jsclub.codefest.sdk.util.MsgPackUtil;

import java.io.IOException;

public class onPlayerInventoryUpdate implements Emitter.Listener {
    private final Inventory heroInventory;
    Gson gson = new Gson();

    public onPlayerInventoryUpdate(Inventory heroInventory) {
        this.heroInventory = heroInventory;
    }

    @Override
    public void call(Object... args) {
        try {
            String message = MsgPackUtil.decode(args[0]);
            InventoryUpdateData inventoryUpdateData = gson.fromJson(message, InventoryUpdateData.class);
            ElementType type = inventoryUpdateData.itemType;
            String id = inventoryUpdateData.id;
            String action = inventoryUpdateData.action;
            switch (action) {
                case "ADD":
                    switch (type) {
                        case GUN:
                            heroInventory.setGun(WeaponFactory.getWeaponById(id));
                            break;
                        case MELEE:
                            heroInventory.setMelee(WeaponFactory.getWeaponById(id));
                            break;
                        case THROWABLE:
                            heroInventory.setThrowable(WeaponFactory.getWeaponById(id));
                            break;
                        case SPECIAL:
                            heroInventory.setSpecial(WeaponFactory.getWeaponById(id));
                            break;
                        case ARMOR:
                            heroInventory.setArmor(ArmorFactory.getArmorById(id));
                            break;
                        case HELMET:
                            heroInventory.setHelmet(ArmorFactory.getArmorById(id));
                            break;
                        case SUPPORT_ITEM:
                            heroInventory.getListSupportItem().add(SupportItemFactory.getSupportItemById(id));
                            break;
                    }
                    break;
                case "REMOVE":
                    switch (type) {
                        case GUN:
                            heroInventory.setGun(null);
                            break;
                        case MELEE:
                            heroInventory.setMelee(WeaponFactory.getWeaponById("HAND"));
                            break;
                        case THROWABLE:
                            heroInventory.setThrowable(null);
                            break;
                        case SPECIAL:
                            heroInventory.setSpecial(null);
                            break;
                        case ARMOR:
                            heroInventory.setArmor(null);
                            break;
                        case HELMET:
                            heroInventory.setHelmet(null);
                            break;
                        case SUPPORT_ITEM:
                            heroInventory.getListSupportItem().remove(SupportItemFactory.getSupportItemById(id));
                            break;
                    }
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
