package jsclub.codefest.sdk.socket.event_handler;

import com.google.gson.Gson;
import io.socket.emitter.Emitter;
import jsclub.codefest.sdk.factory.ArmorFactory;
import jsclub.codefest.sdk.factory.HealingItemFactory;
import jsclub.codefest.sdk.factory.WeaponFactory;
import jsclub.codefest.sdk.model.ElementType;
import jsclub.codefest.sdk.model.Inventory;
import jsclub.codefest.sdk.socket.data.receive_data.ItemData;
import jsclub.codefest.sdk.util.MsgPackUtil;

import java.io.IOException;

public class onPlayerInventoryAdd implements Emitter.Listener {
    private final Inventory inventory;
    Gson gson = new Gson();

    public onPlayerInventoryAdd(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public void call(Object... args) {
        try {
            String message = MsgPackUtil.decode(args[0]);
            ItemData itemData = gson.fromJson(message, ItemData.class);
            ElementType type = itemData.getType();
            String id = itemData.getId();
            System.out.println("Item added: " + id);
            switch (type) {
                case GUN:
                    inventory.setGun(WeaponFactory.getWeaponById(id));
                    break;
                case MELEE:
                    inventory.setMelee(WeaponFactory.getWeaponById(id));
                    break;
                case THROWABLE:
                    inventory.setThrowable(WeaponFactory.getWeaponById(id));
                    break;
                case SPECIAL:
                    inventory.setSpecial(WeaponFactory.getWeaponById(id));
                    break;
                case ARMOR:
                    inventory.setArmor(ArmorFactory.getArmorById(id));
                    break;
                case HELMET:
                    inventory.setHelmet(ArmorFactory.getArmorById(id));
                    break;
                case HEALING_ITEM:
                    inventory.getListHealingItem().add(HealingItemFactory.getHealingItemById(id));
                    break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
