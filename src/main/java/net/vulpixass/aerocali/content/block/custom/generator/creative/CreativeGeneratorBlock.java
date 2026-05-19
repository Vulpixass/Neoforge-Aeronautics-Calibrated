package net.vulpixass.aerocali.content.block.custom.generator.creative;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.block.custom.generator.BaseGeneratorBlock;

public class CreativeGeneratorBlock extends BaseGeneratorBlock {
    public CreativeGeneratorBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public Class getBlockEntityClass() {
        return CreativeGeneratorBlockEntity.class;
    }

    @Override
    public BlockEntityType getBlockEntityType() {
        return AerocaliBlockEntities.CREATIVE_GENERATOR.get();
    }
}
