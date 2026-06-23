package net.vulpixass.aerocali.content.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vulpixass.aerocali.content.block.custom.cable.CableBlock;
import net.vulpixass.aerocali.content.block.custom.cable.CableBlockEntity;
import net.vulpixass.aerocali.content.block.custom.generator.creative.CreativeGeneratorBlock;
import net.vulpixass.aerocali.content.block.custom.generator.industrial.IndustrialGeneratorBlock;
import net.vulpixass.aerocali.content.block.custom.generator.regular.GeneratorBlock;
import net.vulpixass.aerocali.content.block.custom.mechanical_anvil.MechanicalAnvilBlock;
import net.vulpixass.aerocali.content.block.custom.thruster.ThrusterBlock;
import net.vulpixass.aerocali.content.block.custom.nav_tracker.NavigationTrackerBlock;

import static net.vulpixass.aerocali.content.item.AerocaliItems.ITEMS;

public class AerocaliBlocks {
    // Register/Define the Blocks and their according Items
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, "aerocali");

    public static final DeferredHolder<Block, ThrusterBlock> THRUSTER =
            BLOCKS.register("thruster",
                    () -> new ThrusterBlock(Block.Properties.of().strength(2f)));
    public static final DeferredItem<BlockItem> THRUSTER_ITEM = ITEMS.register("thruster",
            () -> new BlockItem(AerocaliBlocks.THRUSTER.get(), new Item.Properties()));

    public static final DeferredHolder<Block, GeneratorBlock> GENERATOR =
            BLOCKS.register("generator",
                    () -> new GeneratorBlock(Block.Properties.of().strength(2f)));
    public static final DeferredItem<BlockItem> GENERATOR_ITEM = ITEMS.register("generator",
            () -> new BlockItem(AerocaliBlocks.GENERATOR.get(), new Item.Properties()));

    public static final DeferredHolder<Block, IndustrialGeneratorBlock> INDUSTRIAL_GENERATOR =
            BLOCKS.register("industrial_generator",
                    () -> new IndustrialGeneratorBlock(Block.Properties.of().strength(2f)));
    public static final DeferredItem<BlockItem> INDUSTRIAL_GENERATOR_ITEM = ITEMS.register("industrial_generator",
            () -> new BlockItem(AerocaliBlocks.INDUSTRIAL_GENERATOR.get(), new Item.Properties()));

    public static final DeferredHolder<Block, CreativeGeneratorBlock> CREATIVE_GENERATOR =
            BLOCKS.register("creative_generator",
                    () -> new CreativeGeneratorBlock(Block.Properties.of().strength(2f)));
    public static final DeferredItem<BlockItem> CREATIVE_GENERATOR_ITEM = ITEMS.register("creative_generator",
            () -> new BlockItem(AerocaliBlocks.CREATIVE_GENERATOR.get(), new Item.Properties()));

    public static final DeferredHolder<Block, NavigationTrackerBlock> TRACKER =
            BLOCKS.register("tracker",
                    () -> new NavigationTrackerBlock(Block.Properties.of().strength(2f)));
    public static final DeferredItem<BlockItem> TRACKER_ITEM = ITEMS.register("tracker",
            () -> new BlockItem(AerocaliBlocks.TRACKER.get(), new Item.Properties()));

    public static final DeferredHolder<Block, MechanicalAnvilBlock> MECHANICAL_ANVIL =
            BLOCKS.register("mechanical_anvil",
                    () -> new MechanicalAnvilBlock(Block.Properties.of().strength(2f), 0));
    public static final DeferredHolder<Block, MechanicalAnvilBlock> LIGHT_MECHANICAL_ANVIL =
            BLOCKS.register("light_mechanical_anvil",
                    () -> new MechanicalAnvilBlock(Block.Properties.of().strength(2f), 1));
    public static final DeferredHolder<Block, MechanicalAnvilBlock> HEAVY_MECHANICAL_ANVIL =
            BLOCKS.register("heavy_mechanical_anvil",
                    () -> new MechanicalAnvilBlock(Block.Properties.of().strength(2f), 2));
    public static final DeferredHolder<Block, MechanicalAnvilBlock> SUPER_HEAVY_MECHANICAL_ANVIL =
            BLOCKS.register("super_heavy_mechanical_anvil",
                    () -> new MechanicalAnvilBlock(Block.Properties.of().strength(2f), 3));
    public static final DeferredItem<BlockItem> MECHANICAL_ANVIL_ITEM = ITEMS.register("mechanical_anvil",
            () -> new BlockItem(AerocaliBlocks.MECHANICAL_ANVIL.get(), new Item.Properties()));

    public static final DeferredHolder<Block, CableBlock> CABLE =
            BLOCKS.register("cable",
                    () -> new CableBlock(Block.Properties.of().strength(2f)));
    public static final DeferredItem<BlockItem> CABLE_ITEM = ITEMS.register("cable",
            () -> new BlockItem(AerocaliBlocks.CABLE.get(), new Item.Properties()));
}
