package net.vulpixass.aerocali.content.item;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vulpixass.aerocali.content.item.custom.*;

import static net.vulpixass.aerocali.AeronauticsCalibrated.MOD_ID;

public class AerocaliItems {
    // Register/Define Items
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);

    // Register Thermal Mechanism Items
    public static final DeferredItem<Item> THERMAL_MECHANISM = ITEMS.register("thermal_mechanism",
            () -> new Item(new Item.Properties()));
    // Inbetween item for the sequenced assembly recipe
    public static final DeferredItem<Item> INCOMPLETE_THERMAL_MECHANISM = ITEMS.register("incomplete_thermal_mechanism",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> IONIZED_THERMAL_MECHANISM = ITEMS.register("ionized_thermal_mechanism",
            () -> new IonUpgradeItem(new Item.Properties()));

    public static final DeferredItem<Item> TRAVERSE_BOARD = ITEMS.register("traverse_board",
            () -> new TraverseBoardItem(new Item.Properties()));

    public static final DeferredItem<Item> NAVIGATION_ELEMENT = ITEMS.register("navigation_element",
            () -> new NavigationElementItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SURVIVAL_PHYSICS_STAFF = ITEMS.register("survival_physics_staff",
            () -> new SurvivalPhysicsStaffItem(new Item.Properties().stacksTo(1).durability(512).rarity(Rarity.UNCOMMON)));
}