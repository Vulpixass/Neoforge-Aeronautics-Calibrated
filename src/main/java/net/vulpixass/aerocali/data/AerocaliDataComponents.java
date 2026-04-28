package net.vulpixass.aerocali.data;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.vulpixass.aerocali.AeronauticsCalibrated;
import net.vulpixass.aerocali.content.item.data.NavTargetData;

public class AerocaliDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, AeronauticsCalibrated.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<NavTargetData>> NAV_TARGET =
            DATA_COMPONENTS.register("nav_target", () ->
                    DataComponentType.<NavTargetData>builder()
                            .persistent(NavTargetData.CODEC)
                            .networkSynchronized(NavTargetData.STREAM_CODEC)
                            .build()
            );
}
