package net.vulpixass.aerocali;

import dev.simulated_team.simulated.content.blocks.nav_table.navigation_target.NavigationTarget;
import dev.simulated_team.simulated.index.SimDataComponents;
import dev.simulated_team.simulated.index.SimRegistries;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.vulpixass.aerocali.capabilities.AerocaliCapabilities;
import net.vulpixass.aerocali.compat.NavElementRegistry;
import net.vulpixass.aerocali.compat.NavTarget;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.AerocaliTabs;
import net.vulpixass.aerocali.content.block.AerocaliBlocks;
import net.vulpixass.aerocali.content.block.custom.generator.GeneratorBlock;
import net.vulpixass.aerocali.content.item.AerocaliItems;
import net.vulpixass.aerocali.content.item.data.NavDataStorage;
import net.vulpixass.aerocali.content.item.data.NavTargetData;
import net.vulpixass.aerocali.content.model.PartialModels;
import net.vulpixass.aerocali.content.particle.AerocaliParticles;
import net.vulpixass.aerocali.content.recipe.AerocaliRecipes;
import net.vulpixass.aerocali.content.sound.AerocaliSounds;
import net.vulpixass.aerocali.data.AerocaliDataComponents;
import net.vulpixass.aerocali.network.NavUpdatePayload;
import net.vulpixass.aerocali.network.ServerPayloadHandler;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.function.Function;

import static net.vulpixass.aerocali.data.AerocaliDataComponents.NAV_TARGET;
import static net.vulpixass.aerocali.data.AerocaliDataComponents.NAV_TARGET_DATA;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(AeronauticsCalibrated.MOD_ID)
public class AeronauticsCalibrated {
    public static final String MOD_ID = "aerocali";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static NavigationTarget AEROCALI_NAV_BRIDGE;

    /*

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "aerocali" namespace

    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "aerocali" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "aerocali:example_block", combining the namespace and path
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // Creates a new BlockItem with the id "aerocali:example_block", combining the namespace and path
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    // Creates a new food item with the id "aerocali:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // Creates a creative tab with the id "aerocali:example_tab" for the example item, that is placed after the combat tab


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.

     */
    public AeronauticsCalibrated(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(this::onRegister);
        modEventBus.addListener(this::register);
        AerocaliDataComponents.DATA_COMPONENTS.register(modEventBus);

        AerocaliBlocks.BLOCKS.register(modEventBus);
        AerocaliItems.ITEMS.register(modEventBus);
        AerocaliTabs.AEROCALI_TABS.register(modEventBus);

        AerocaliBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        PartialModels.init();
        AerocaliRecipes.register(modEventBus);

        AerocaliParticles.register(modEventBus);
        AerocaliSounds.SOUNDS.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NavElementRegistry.init();
        event.enqueueWork(() -> {
            NAV_TARGET_DATA = new NavDataStorage<>(NAV_TARGET.get(),
                    () -> new NavTargetData(0, 0, 0, "minecraft:overworld"),
                    Function.identity());
        });

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(AerocaliBlocks.THRUSTER_ITEM);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(AerocaliItems.IONIZED_THERMAL_MECHANISM);
            event.accept(AerocaliItems.NAVIGATION_ELEMENT);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(AerocaliItems.THERMAL_MECHANISM);
            event.accept(AerocaliItems.IONIZED_THERMAL_MECHANISM);
        }
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(AerocaliBlocks.THRUSTER_ITEM);
            event.accept(AerocaliBlocks.GENERATOR_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        System.out.println("Aerocali locked and loaded");
    }

    @SubscribeEvent
    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(AerocaliCapabilities.ENERGY, AerocaliBlockEntities.THRUSTER.get(),
                (thruster, ctx) -> thruster.getEnergyStorage());
        event.registerBlockEntity(AerocaliCapabilities.ENERGY, AerocaliBlockEntities.GENERATOR.get(),
                (generator, ctx) -> generator.getEnergyStorage());

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, AerocaliBlockEntities.THRUSTER.get(),
                (thruster, side) -> {
                    if (side == null) return null;

                    Direction back = thruster.getBlockDirection().getOpposite();
                    if (side == back) return thruster.getEnergyStorage();

                    return null;
                });

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, AerocaliBlockEntities.GENERATOR.get(),
                (generator, side) -> {
                    if (side == null) return generator.getEnergyStorage();

                    Direction facing = generator.getBlockState().getValue(GeneratorBlock.FACING);
                    if (side.getAxis() != facing.getAxis()) {
                        return generator.getEnergyStorage();
                    }

                    return null;
                });
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AerocaliBlockEntities.TRACKER.get(),
                (be, side) -> be.getInventory());
    }
    @SubscribeEvent
    public static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        event.modify(AerocaliItems.NAVIGATION_ELEMENT.get(), (builder) -> builder.set(SimDataComponents.TARGET, new NavTarget()));
    }

    @SubscribeEvent
    public void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("aerocali").versioned("1.0");
        registrar.playToServer(NavUpdatePayload.TYPE, NavUpdatePayload.STREAM_CODEC, ServerPayloadHandler::handleNavUpdate);
    }

    @SubscribeEvent
    public void onRegister(RegisterEvent event) {
        if (event.getRegistryKey().equals(SimRegistries.Keys.NAVIGATION_TARGET)) {
            AEROCALI_NAV_BRIDGE = new NavTarget();
            event.register(SimRegistries.Keys.NAVIGATION_TARGET, ResourceLocation.fromNamespaceAndPath("aerocali", "target"),
                    () -> AEROCALI_NAV_BRIDGE);
            System.out.println("DEBUG: Registered Aerocali Navigation Bridge!");
        }
    }
}