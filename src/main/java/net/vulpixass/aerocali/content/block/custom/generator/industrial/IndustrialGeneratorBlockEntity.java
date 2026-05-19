package net.vulpixass.aerocali.content.block.custom.generator.industrial;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.block.custom.generator.BaseGeneratorBlockEntity;

public class IndustrialGeneratorBlockEntity extends BaseGeneratorBlockEntity {
    public IndustrialGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(AerocaliBlockEntities.INDUSTRIAL_GENERATOR.get(), pos, state);
        generationFactor = 2f;
        FELimit = 180;
    }
}
