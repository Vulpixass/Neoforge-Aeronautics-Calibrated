package net.vulpixass.aerocali.content.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.vulpixass.aerocali.content.item.AerocaliItems.ITEMS;

public class AerocaliBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, "aerocali");

    public static final DeferredHolder<Block, ThrusterBlock> THRUSTER =
            BLOCKS.register("thruster",
                    () -> new ThrusterBlock(Block.Properties.of().strength(2f)));
    public static final DeferredItem<BlockItem> THRUSTER_ITEM = ITEMS.register("thruster",
            () -> new BlockItem(AerocaliBlocks.THRUSTER.get(), new Item.Properties()));

}
