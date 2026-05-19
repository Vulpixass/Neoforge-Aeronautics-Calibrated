package net.vulpixass.aerocali.content.block.custom.generator.creative;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.block.custom.generator.BaseGeneratorBlockEntity;

public class CreativeGeneratorBlockEntity extends BaseGeneratorBlockEntity {
    public CreativeGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(AerocaliBlockEntities.CREATIVE_GENERATOR.get(), pos, state);
        generationFactor = 10000000000f;
        FELimit = 100000000;
        infiniteEnergy = true;
    }

    @Override
    public float calculateStressApplied() {
        return 1;
    }
}
