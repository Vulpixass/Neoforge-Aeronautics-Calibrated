package net.vulpixass.aerocali.content.block.custom.generator.regular;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.block.custom.generator.BaseGeneratorBlockEntity;

public class GeneratorBlockEntity extends BaseGeneratorBlockEntity {
    public GeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(AerocaliBlockEntities.GENERATOR.get(), pos, state);
    }
}
