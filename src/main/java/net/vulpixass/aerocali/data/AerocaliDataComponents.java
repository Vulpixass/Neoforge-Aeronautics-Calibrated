package net.vulpixass.aerocali.data;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vulpixass.aerocali.AeronauticsCalibrated;
import net.vulpixass.aerocali.content.item.data.NavDataStorage;
import net.vulpixass.aerocali.content.item.data.NavTargetData;

public class AerocaliDataComponents {
    // Define/Register the mod required Data Components
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, AeronauticsCalibrated.MOD_ID);

    // Navigation Target Data Component defining at which Pos and Dimension the Target is
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<NavTargetData>> NAV_TARGET = DATA_COMPONENTS
            .register("nav_target", () -> DataComponentType.<NavTargetData>builder().persistent(NavTargetData.CODEC)
                    .networkSynchronized(NavTargetData.STREAM_CODEC).build());

    public static NavDataStorage<NavTargetData> NAV_TARGET_DATA;
}
