package net.vulpixass.aerocali.content.block.render.generator;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;
import net.vulpixass.aerocali.content.block.custom.generator.GeneratorBlock;
import net.vulpixass.aerocali.content.block.custom.generator.GeneratorBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class GeneratorVisual extends KineticBlockEntityVisual<GeneratorBlockEntity> {
    private RotatingInstance shaft;

    public GeneratorVisual(VisualizationContext context, GeneratorBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        Direction.Axis axis = blockState.getValue(GeneratorBlock.FACING).getAxis();

        Instancer<RotatingInstance> instancer = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT));
        shaft = instancer.createInstance();
        shaft.setup(blockEntity, axis, blockEntity.getSpeed())
                .setPosition(getVisualPosition())
                .setChanged();
    }

    @Override
    public void update(float pt) {
        Direction.Axis axis = blockState.getValue(GeneratorBlock.FACING).getAxis();
        shaft.setup(blockEntity, axis, blockEntity.getSpeed()).setChanged();
    }

    @Override
    protected void _delete() {
        if (shaft != null) shaft.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {

    }

    @Override
    public void updateLight(float partialTick) {

    }
}