package net.vulpixass.aerocali.content;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vulpixass.aerocali.content.block.AerocaliBlocks;
import net.vulpixass.aerocali.content.item.AerocaliItems;

public class AerocaliTabs {
    public static final DeferredRegister<CreativeModeTab> AEROCALI_TABS;
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> AEROCALI_TAB;

    static {
        AEROCALI_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "aerocali");
        AEROCALI_TAB = AEROCALI_TABS.register("aerocali_tab", () -> CreativeModeTab.builder().title(Component.translatable
                ("itemGroup.aerocali_tab")).icon(() ->
                        new ItemStack( AerocaliItems.THERMAL_MECHANISM.get())).displayItems((parameters, output) -> {
            output.accept(AerocaliItems.NAVIGATION_ELEMENT.get());
            output.accept(AerocaliItems.THERMAL_MECHANISM.get());
            output.accept(AerocaliItems.IONIZED_THERMAL_MECHANISM.get());
            output.accept(AerocaliBlocks.THRUSTER_ITEM.get());
            output.accept(AerocaliBlocks.GENERATOR_ITEM.get());
            output.accept(AerocaliBlocks.TRACKER_ITEM.get());
            output.accept(AerocaliItems.TRAVERSE_BOARD.get());
        }).build());
    }
}
