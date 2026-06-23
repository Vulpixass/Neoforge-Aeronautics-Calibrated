package net.vulpixass.aerocali;

import com.mojang.logging.LogUtils;
import dev.simulated_team.simulated.content.blocks.nav_table.navigation_target.NavigationTarget;
import dev.simulated_team.simulated.index.SimDataComponents;
import dev.simulated_team.simulated.index.SimRegistries;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.vulpixass.aerocali.capabilities.AerocaliCapabilities;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.AerocaliTabs;
import net.vulpixass.aerocali.content.block.AerocaliBlocks;
import net.vulpixass.aerocali.content.block.custom.generator.creative.CreativeGeneratorBlock;
import net.vulpixass.aerocali.content.block.custom.generator.industrial.IndustrialGeneratorBlock;
import net.vulpixass.aerocali.content.block.custom.generator.regular.GeneratorBlock;
import net.vulpixass.aerocali.content.item.AerocaliItems;
import net.vulpixass.aerocali.content.item.data.NavDataStorage;
import net.vulpixass.aerocali.content.item.data.NavTarget;
import net.vulpixass.aerocali.content.item.data.NavTargetData;
import net.vulpixass.aerocali.content.model.PartialModels;
import net.vulpixass.aerocali.content.particle.AerocaliParticles;
import net.vulpixass.aerocali.content.recipe.AerocaliRecipes;
import net.vulpixass.aerocali.content.sound.AerocaliSounds;
import net.vulpixass.aerocali.data.AerocaliDataComponents;
import net.vulpixass.aerocali.network.packet.DamageStaffPayload;
import net.vulpixass.aerocali.network.packet.NavUpdatePayload;
import net.vulpixass.aerocali.network.ServerPayloadHandler;
import net.vulpixass.aerocali.util.config.AerocaliAllConfigs;
import org.slf4j.Logger;

import java.util.function.Function;

import static net.vulpixass.aerocali.data.AerocaliDataComponents.NAV_TARGET;
import static net.vulpixass.aerocali.data.AerocaliDataComponents.NAV_TARGET_DATA;

@Mod(AeronauticsCalibrated.MOD_ID)
public class AeronauticsCalibrated {
    public static final String MOD_ID = "aerocali";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static NavigationTarget AEROCALI_NAV_BRIDGE;

    public AeronauticsCalibrated(IEventBus modEventBus, ModContainer modContainer) {
        // Register the methods required for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::onRegister);
        modEventBus.addListener(this::register);
        AerocaliDataComponents.DATA_COMPONENTS.register(modEventBus);

        ModContainer container = ModLoadingContext.get().getActiveContainer();
        AerocaliAllConfigs.register(ModLoadingContext.get(), container);
        modContainer.registerConfig(ModConfig.Type.STARTUP, AerocaliAllConfigs.SPEC);

        AerocaliBlocks.BLOCKS.register(modEventBus);
        AerocaliItems.ITEMS.register(modEventBus);
        AerocaliTabs.AEROCALI_TABS.register(modEventBus);

        AerocaliBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        PartialModels.init();
        AerocaliRecipes.register(modEventBus);

        AerocaliParticles.register(modEventBus);
        AerocaliSounds.SOUNDS.register(modEventBus);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NAV_TARGET_DATA = new NavDataStorage<>(NAV_TARGET.get(), () -> new NavTargetData(0, 0, 0, "minecraft:overworld"),
                    Function.identity());
        });
    }

    // Add all the items to creative tabs
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(AerocaliBlocks.THRUSTER_ITEM);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(AerocaliItems.IONIZED_THERMAL_MECHANISM);
            event.accept(AerocaliItems.NAVIGATION_ELEMENT);
            event.accept(AerocaliItems.SURVIVAL_PHYSICS_STAFF);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(AerocaliItems.THERMAL_MECHANISM);
            event.accept(AerocaliItems.IONIZED_THERMAL_MECHANISM);
        }
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(AerocaliBlocks.THRUSTER_ITEM);
            event.accept(AerocaliBlocks.GENERATOR_ITEM);
            event.accept(AerocaliBlocks.INDUSTRIAL_GENERATOR_ITEM);
            event.accept(AerocaliBlocks.CREATIVE_GENERATOR_ITEM);
            event.accept(AerocaliBlocks.MECHANICAL_ANVIL_ITEM);
            event.accept(AerocaliBlocks.CABLE_ITEM);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        System.out.println("Aerocali locked and loaded");
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        // Make the BlockEntities connect to wires from other mods
        event.registerBlockEntity(AerocaliCapabilities.ENERGY, AerocaliBlockEntities.THRUSTER.get(),
                (thruster, ctx) -> thruster.getEnergyStorage());
        event.registerBlockEntity(AerocaliCapabilities.ENERGY, AerocaliBlockEntities.GENERATOR.get(),
                (generator, ctx) -> generator.getEnergyStorage());
        event.registerBlockEntity(AerocaliCapabilities.ENERGY, AerocaliBlockEntities.INDUSTRIAL_GENERATOR.get(),
                (generator, ctx) -> generator.getEnergyStorage());
        event.registerBlockEntity(AerocaliCapabilities.ENERGY, AerocaliBlockEntities.CREATIVE_GENERATOR.get(),
                (generator, ctx) -> generator.getEnergyStorage());

    event.registerBlockEntity(AerocaliCapabilities.ENERGY, AerocaliBlockEntities.CABLE.get(),
                (cable, ctx) -> cable.getEnergyStorage());

        // Depict where the wires can connect to power the Thruster
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, AerocaliBlockEntities.THRUSTER.get(),
                (thruster, side) -> {
                    if (side == null) return null;

                    Direction back = thruster.getBlockDirection().getOpposite();
                    if (side == back) return thruster.getEnergyStorage();

                    return null;
                });

        // Depict where the wires can connect to power the Generators
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, AerocaliBlockEntities.GENERATOR.get(),
                (generator, side) -> {
                    if (side == null) return generator.getEnergyStorage();

                    Direction facing = generator.getBlockState().getValue(GeneratorBlock.FACING);
                    if (side.getAxis() != facing.getAxis()) {
                        return generator.getEnergyStorage();
                    }

                    return null;
                });
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, AerocaliBlockEntities.INDUSTRIAL_GENERATOR.get(),
                (generator, side) -> {
                    if (side == null) return generator.getEnergyStorage();

                    Direction facing = generator.getBlockState().getValue(IndustrialGeneratorBlock.FACING);
                    if (side.getAxis() != facing.getAxis()) {
                        return generator.getEnergyStorage();
                    }

                    return null;
                });
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, AerocaliBlockEntities.CREATIVE_GENERATOR.get(),
                (generator, side) -> {
                    if (side == null) return generator.getEnergyStorage();

                    Direction facing = generator.getBlockState().getValue(CreativeGeneratorBlock.FACING);
                    if (side.getAxis() != facing.getAxis()) {
                        return generator.getEnergyStorage();
                    }

                    return null;
                });
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, AerocaliBlockEntities.CABLE.get(),
                (blockEntity, direction) -> blockEntity.getEnergyStorage());
        // Register the Inventory of the BlockEntity
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AerocaliBlockEntities.TRACKER.get(),
                (be, side) -> be.getInventory());
    }
    @SubscribeEvent
    public static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        // Give the Navigation Compass a default state
        event.modify(AerocaliItems.NAVIGATION_ELEMENT.get(), (builder) -> builder.set(SimDataComponents.TARGET, new NavTarget()));
    }

    @SubscribeEvent
    public void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("aerocali").versioned("1.0");

        // Register the C2S packet Payload
        registrar.playToServer(NavUpdatePayload.TYPE, NavUpdatePayload.STREAM_CODEC, ServerPayloadHandler::handleNavUpdate);
        registrar.playToServer(DamageStaffPayload.TYPE, DamageStaffPayload.CODEC, ServerPayloadHandler::handleStaffDamage);
    }

    @SubscribeEvent
    public void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(SimRegistries.Keys.NAVIGATION_TARGET)) {
            AEROCALI_NAV_BRIDGE = new NavTarget();
            event.register(SimRegistries.Keys.NAVIGATION_TARGET, ResourceLocation.fromNamespaceAndPath("aerocali", "target"),
                    () -> AEROCALI_NAV_BRIDGE);
        }
    }
}