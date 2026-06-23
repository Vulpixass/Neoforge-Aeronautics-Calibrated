package net.vulpixass.aerocali.content.block.custom.mechanical_anvil;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import dev.simulated_team.simulated.index.SimSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.block.AerocaliBlocks;

public class MechanicalAnvilBlockEntity extends KineticBlockEntity {
    public float clientHeight = 0;
    private double lastRPM = 0;
    private int wait = 0;

    public MechanicalAnvilBlockEntity(BlockPos pos, BlockState state) {
        super(AerocaliBlockEntities.MECHANICAL_ANVIL.get(), pos, state);
        // initialize client height based on current RPM
        if (level != null && level.isClientSide) {
            float rpm = Math.abs(getSpeed());
            clientHeight = Mth.clamp(rpm / 128f, 0f, 1f) * 0.35f;
        }
    }


    @Override
    public void tick() {
        super.tick();

        double rpm = Math.abs(getSpeed());
        int level = computeMassLevel(rpm);

        BlockState state = getBlockState();
        int oldLevel = state.getValue(MechanicalAnvilBlock.MASS);

        // on Mass changed -> swap block for physics
        if (oldLevel != level && wait == 0) {
            // play sound
            // pick correct block type for physics
            BlockState newState = switch (level) {
                case 1 -> AerocaliBlocks.LIGHT_MECHANICAL_ANVIL.get().defaultBlockState();
                case 2 -> AerocaliBlocks.HEAVY_MECHANICAL_ANVIL.get().defaultBlockState();
                case 3 -> AerocaliBlocks.SUPER_HEAVY_MECHANICAL_ANVIL.get().defaultBlockState();
                default -> AerocaliBlocks.MECHANICAL_ANVIL.get().defaultBlockState();
            };

            newState = newState.setValue(MechanicalAnvilBlock.MASS, level);
            getLevel().setBlock(worldPosition, newState, 3);

            wait = 10;
        }

        if (lastRPM != rpm) {
            getLevel().playSound(null, worldPosition, SimSoundEvents.DOCKING_CONNECTOR_EXTENDS.event(), SoundSource.BLOCKS);
        }

        if (wait > 0) wait--;
        lastRPM = rpm;
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        if (clientPacket) {
            tag.putFloat("ClientHeight", clientHeight);
        }
    }

    @Override
    public void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        if (clientPacket && tag.contains("ClientHeight")) {
            clientHeight = tag.getFloat("ClientHeight");
        }

    }

    private int computeMassLevel(double rpm) {
        if (rpm < 32) return 0;
        if (rpm < 64) return 1;
        if (rpm < 128) return 2;
        return 3;
    }
}
