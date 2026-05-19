package net.vulpixass.aerocali.content.block.custom.generator.regular;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.block.custom.generator.BaseGeneratorBlock;

public class GeneratorBlock extends BaseGeneratorBlock {
    public GeneratorBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public Class getBlockEntityClass() {
        return GeneratorBlockEntity.class;
    }

    @Override
    public BlockEntityType getBlockEntityType() {
        return AerocaliBlockEntities.GENERATOR.get();
    }
}
