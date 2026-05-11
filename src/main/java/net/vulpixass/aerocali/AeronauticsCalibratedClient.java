package net.vulpixass.aerocali;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.vulpixass.aerocali.client.AerocaliItemProperties;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.block.AerocaliBlocks;
import net.vulpixass.aerocali.content.block.render.generator.GeneratorRenderer;
import net.vulpixass.aerocali.content.block.render.nav_tracker.NavigationTrackerRenderer;
import net.vulpixass.aerocali.content.item.AerocaliItems;
import net.vulpixass.aerocali.content.item.render.nav_tracker.NavigationTrackerItemRenderer;
import net.vulpixass.aerocali.content.particle.AerocaliParticles;
import net.vulpixass.aerocali.content.particle.ThrustParticles;
import net.vulpixass.aerocali.data.AerocaliDataComponents;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = AeronauticsCalibrated.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = AeronauticsCalibrated.MOD_ID, value = Dist.CLIENT)
public class AeronauticsCalibratedClient {
    public AeronauticsCalibratedClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        AerocaliItemProperties.register();
        BlockEntityRenderers.register(AerocaliBlockEntities.GENERATOR.get(), GeneratorRenderer::new);
        BlockEntityRenderers.register(AerocaliBlockEntities.TRACKER.get(), NavigationTrackerRenderer::new);
        event.enqueueWork(() -> {
            ItemProperties.register(AerocaliItems.TRAVERSE_BOARD.get(), ResourceLocation.tryBuild("aerocali", "written"),
                    (stack, level, entity, seed) -> stack.has(AerocaliDataComponents.NAV_TARGET.get()) ? 1.0F : 0.0F);
        });
    }
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(AerocaliParticles.THRUST_PARTICLES.get(), ThrustParticles.Provider::new);
    }
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(AerocaliBlockEntities.GENERATOR.get(), GeneratorRenderer::new);
        event.registerBlockEntityRenderer(AerocaliBlockEntities.TRACKER.get(), NavigationTrackerRenderer::new);
    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new NavigationTrackerItemRenderer();
            }
        }, AerocaliBlocks.TRACKER_ITEM.get());
    }
}
