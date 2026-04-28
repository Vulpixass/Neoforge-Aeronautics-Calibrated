package net.vulpixass.aerocali;

import net.minecraft.core.Direction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.vulpixass.aerocali.capabilities.AerocaliCapabilities;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.block.AerocaliBlocks;
import net.vulpixass.aerocali.content.item.AerocaliItems;
import net.vulpixass.aerocali.content.particle.AerocaliParticles;
import net.vulpixass.aerocali.content.sound.AerocaliSounds;
import net.vulpixass.aerocali.data.AerocaliDataComponents;
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

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(AeronauticsCalibrated.MOD_ID)
public class AeronauticsCalibrated {
    public static final String MOD_ID = "aerocali";
    public static final Logger LOGGER = LogUtils.getLogger();

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
        AerocaliBlocks.BLOCKS.register(modEventBus);
        AerocaliItems.ITEMS.register(modEventBus);

        AerocaliBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        AerocaliParticles.register(modEventBus);
        AerocaliSounds.SOUNDS.register(modEventBus);

        AerocaliDataComponents.DATA_COMPONENTS.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.accept(AerocaliBlocks.THRUSTER_ITEM);
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(AerocaliItems.ION_UPGRADE);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(AerocaliItems.THERMAL_MECHANISM);
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

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, AerocaliBlockEntities.THRUSTER.get(),
                (thruster, side) -> {
                    if (side == null) return null;

                    Direction back = thruster.getBlockDirection().getOpposite();
                    if (side == back) return thruster.getEnergyStorage();

                    return null;
                });
    }
}
