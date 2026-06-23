package net.vulpixass.aerocali.util.config;

import net.createmod.catnip.config.ConfigBase;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

@EventBusSubscriber
public class AerocaliAllConfigs {

    private static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC = BUILDER.build();
    public static AerocaliConfigCommon common;

    private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type type) {
        Pair<T, ModConfigSpec> specPair =
                new ModConfigSpec.Builder().configure(builder -> {
                    T config = factory.get();
                    config.registerAll(builder);
                    return config;
                });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(type, config);
        return config;
    }

    public static void register(ModLoadingContext context, ModContainer container) {
        common = register(AerocaliConfigCommon::new, ModConfig.Type.COMMON);

        for (Map.Entry<ModConfig.Type, ConfigBase> entry : CONFIGS.entrySet()) {
            container.registerConfig(entry.getKey(), entry.getValue().specification);
        }
    }

    @SubscribeEvent
    public static void onLoad(ModConfigEvent.Loading event) {
        CONFIGS.values().forEach(config -> {
            if (config.specification == event.getConfig().getSpec())
                config.onLoad();
        });
    }

    @SubscribeEvent
    public static void onReload(ModConfigEvent.Reloading event) {
        CONFIGS.values().forEach(config -> {
            if (config.specification == event.getConfig().getSpec())
                config.onReload();
        });
    }
}