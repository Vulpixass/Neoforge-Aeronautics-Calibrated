package net.vulpixass.aerocali;

import net.createmod.ponder.foundation.PonderIndex;
import net.createmod.ponder.foundation.registration.PonderLocalization;
import net.createmod.ponder.foundation.registration.PonderTagRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
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
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.vulpixass.aerocali.client.AerocaliItemProperties;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.block.AerocaliBlocks;
import net.vulpixass.aerocali.content.block.render.generator.GeneratorRenderer;
import net.vulpixass.aerocali.content.block.render.mechanical_anvil.MechanicalAnvilRenderer;
import net.vulpixass.aerocali.content.block.render.nav_tracker.NavigationTrackerRenderer;
import net.vulpixass.aerocali.content.item.AerocaliItems;
import net.vulpixass.aerocali.content.particle.AerocaliParticles;
import net.vulpixass.aerocali.content.particle.ThrustParticles;
import net.vulpixass.aerocali.content.ponder.AerocaliPonderPlugin;
import net.vulpixass.aerocali.data.AerocaliDataComponents;

import static net.vulpixass.aerocali.AeronauticsCalibrated.MOD_ID;

@Mod(value = MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
public class AeronauticsCalibratedClient {
    public AeronauticsCalibratedClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Add a custom Ponder Plugin to enable Ponder Scene creation
        PonderIndex.addPlugin(new AerocaliPonderPlugin());
        PonderTagRegistry registry = new PonderTagRegistry(new PonderLocalization());
        // Register the Item Properites
        AerocaliItemProperties.register();

        // Register the BlockEntityRenderers for the BlockEntities that need one
        BlockEntityRenderers.register(AerocaliBlockEntities.GENERATOR.get(), GeneratorRenderer::new);
        BlockEntityRenderers.register(AerocaliBlockEntities.INDUSTRIAL_GENERATOR.get(), GeneratorRenderer::new);
        BlockEntityRenderers.register(AerocaliBlockEntities.CREATIVE_GENERATOR.get(), GeneratorRenderer::new);
        BlockEntityRenderers.register(AerocaliBlockEntities.TRACKER.get(), NavigationTrackerRenderer::new);
        BlockEntityRenderers.register(AerocaliBlockEntities.MECHANICAL_ANVIL.get(), MechanicalAnvilRenderer::new);

        event.enqueueWork(() -> {
            ItemProperties.register(AerocaliItems.TRAVERSE_BOARD.get(), ResourceLocation.tryBuild("aerocali", "written"),
                    (stack, level, entity, seed) -> stack.has(AerocaliDataComponents.NAV_TARGET.get()) ? 1.0F : 0.0F);
            ItemBlockRenderTypes.setRenderLayer(AerocaliBlocks.CABLE.get(), RenderType.cutout());
        });
    }
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        // Register (unused) custom Thruster Particles
        event.registerSpriteSet(AerocaliParticles.THRUST_PARTICLES.get(), ThrustParticles.Provider::new);
    }
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // I guess just render the BlockEntityRenderers again IDR how this worked XD!
        event.registerBlockEntityRenderer(AerocaliBlockEntities.GENERATOR.get(), GeneratorRenderer::new);
        event.registerBlockEntityRenderer(AerocaliBlockEntities.INDUSTRIAL_GENERATOR.get(), GeneratorRenderer::new);
        event.registerBlockEntityRenderer(AerocaliBlockEntities.CREATIVE_GENERATOR.get(), GeneratorRenderer::new);
        event.registerBlockEntityRenderer(AerocaliBlockEntities.TRACKER.get(), NavigationTrackerRenderer::new);
        event.registerBlockEntityRenderer(AerocaliBlockEntities.MECHANICAL_ANVIL.get(), MechanicalAnvilRenderer::new);
    }
}