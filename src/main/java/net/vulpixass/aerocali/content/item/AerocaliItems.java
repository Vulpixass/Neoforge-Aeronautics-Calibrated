package net.vulpixass.aerocali.content.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vulpixass.aerocali.content.item.custom.IonUpgradeItem;
import net.vulpixass.aerocali.content.item.custom.NavigationElementItem;
import net.vulpixass.aerocali.content.item.custom.TraverseBoardItem;

import static net.vulpixass.aerocali.AeronauticsCalibrated.MOD_ID;

public class AerocaliItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    public static final DeferredItem<Item> THERMAL_MECHANISM = ITEMS.register("thermal_mechanism",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> INCOMPLETE_THERMAL_MECHANISM = ITEMS
            .register("incomplete_thermal_mechanism", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> IONIZED_THERMAL_MECHANISM = ITEMS.register("ionized_thermal_mechanism",
            () -> new IonUpgradeItem(new Item.Properties()));

    public static final DeferredItem<Item> TRAVERSE_BOARD = ITEMS.register("traverse_board",
            () -> new TraverseBoardItem(new Item.Properties()));

    public static final DeferredItem<Item> NAVIGATION_ELEMENT = ITEMS
            .register("navigation_element", () -> new NavigationElementItem(new Item.Properties().stacksTo(1)));
}