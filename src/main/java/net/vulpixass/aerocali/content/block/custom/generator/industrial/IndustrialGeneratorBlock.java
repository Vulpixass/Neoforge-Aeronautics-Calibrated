package net.vulpixass.aerocali.content.block.custom.generator.industrial;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.block.custom.generator.BaseGeneratorBlock;

public class IndustrialGeneratorBlock extends BaseGeneratorBlock {
    public IndustrialGeneratorBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public Class getBlockEntityClass() {
        return IndustrialGeneratorBlockEntity.class;
    }

    @Override
    public BlockEntityType getBlockEntityType() {
        return AerocaliBlockEntities.INDUSTRIAL_GENERATOR.get();
    }
}