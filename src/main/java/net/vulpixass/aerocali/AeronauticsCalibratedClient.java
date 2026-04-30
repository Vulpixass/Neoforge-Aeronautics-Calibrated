package net.vulpixass.aerocali;

import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.visualization.VisualizerRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.vulpixass.aerocali.client.AerocaliItemProperties;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.block.render.GeneratorRenderer;
import net.vulpixass.aerocali.content.block.render.GeneratorVisual;
import net.vulpixass.aerocali.content.particle.AerocaliParticles;
import net.vulpixass.aerocali.content.particle.ThrustParticles;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = AeronauticsCalibrated.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = AeronauticsCalibrated.MOD_ID, value = Dist.CLIENT)
public class AeronauticsCalibratedClient {
    public AeronauticsCalibratedClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        AerocaliItemProperties.register();
        BlockEntityRenderers.register(AerocaliBlockEntities.GENERATOR.get(), GeneratorRenderer::new);

        // 2. This handles the "Flywheel" rendering (the spinning shaft)
        // Note: The method name might vary slightly depending on your Flywheel version
        // It is often part of the Instancer/Visual registration

    }
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(AerocaliParticles.THRUST_PARTICLES.get(), ThrustParticles.Provider::new);
    }
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(AerocaliBlockEntities.GENERATOR.get(), GeneratorRenderer::new);
    }
}
