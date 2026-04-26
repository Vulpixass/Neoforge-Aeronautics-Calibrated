package net.vulpixass.aerocali.content.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vulpixass.aerocali.content.item.custom.IonUpgradeItem;

import static net.vulpixass.aerocali.AeronauticsCalibrated.MOD_ID;

public class AerocaliItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredItem<Item> ION_UPGRADE = ITEMS.register("ion_upgrade",
            () -> new IonUpgradeItem(new Item.Properties()));
}
