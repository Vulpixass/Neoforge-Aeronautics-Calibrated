package net.vulpixass.aerocali.content.block;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.vulpixass.aerocali.capabilities.AerocaliCapabilities;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.sound.GeneratorSoundInstance;

import java.util.List;

public class GeneratorBlockEntity extends KineticBlockEntity {
    private Object generatorSound;
    private final GeneratorEnergy energy = new GeneratorEnergy(5000, 500, 500);

    public GeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(AerocaliBlockEntities.GENERATOR.get(), pos, state);
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        setChanged();
    }


    @Override
    public void tick() {
        super.tick();
        if (level == null) return;
        if (level.isClientSide) {
            manageSound();
        }

        if (level.isClientSide) return;

        if (level.getGameTime() % 4 == 0) {
            float speed = Math.abs(getSpeed());
            if (speed > 0) {
                int generated = (int) Math.min(60, speed * 0.75f);

                energy.receiveEnergy(generated, false);
                setChanged();
                sendData();


                //System.out.println("Generated: " + generated + " FE (4-tick cycle)");
            }
        }

        if (energy.getEnergyStored() > 0) {
            distributeEnergy();
            sendData();
        }
    }


    private void distributeEnergy() {
        Direction facing = getBlockState().getValue(GeneratorBlock.FACING);
        Direction.Axis rotationAxis = facing.getAxis();

        for (Direction side : Direction.values()) {
            if (side.getAxis() == rotationAxis) continue;

            BlockPos targetPos = worldPosition.relative(side);
            IEnergyStorage targetStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, targetPos, side.getOpposite());

            if (targetStorage != null && targetStorage.canReceive()) {
                int toSend = energy.extractEnergy(1000, true);
                int accepted = targetStorage.receiveEnergy(toSend, false);
                energy.extractEnergy(accepted, false);
                setChanged();
            }
        }
    }

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putInt("Energy", energy.getEnergyStored());
    }

    @Override
    public void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        energy.setEnergy(tag.getInt("Energy"));
    }

    @Override
    public float calculateStressApplied() {
        return 4.0f;
    }

    @Override
    public float calculateAddedStressCapacity() {
        return 0;
    }

    private static class GeneratorEnergy extends EnergyStorage {
        public GeneratorEnergy(int capacity, int maxReceive, int maxExtract) {
            super(capacity, maxReceive, maxExtract);
        }
        public void setEnergy(int amount) {
            this.energy = Math.min(capacity, Math.max(0, amount));
        }
    }

    public IEnergyStorage getEnergyStorage() {
        return energy;
    }

    private void manageSound() {
        float speed = Math.abs(getSpeed());

        // Start sound if rotating and not already playing
        if (speed > 0.01f && generatorSound == null) {
            GeneratorSoundInstance sound = new GeneratorSoundInstance(this);
            net.minecraft.client.Minecraft.getInstance().getSoundManager().play(sound);
            generatorSound = sound;
        }
        // Clear reference if the sound stopped itself
        else if (generatorSound instanceof GeneratorSoundInstance sound && sound.isStopped()) {
            generatorSound = null;
        }
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        tooltip.add(Component.literal(" "));
        tooltip.add(Component.literal("    Generator Stats:").withStyle(ChatFormatting.GRAY));

        float speed = Math.abs(getSpeed());
        int currentGen = (int) Math.min(60, speed * 0.75f);

        tooltip.add(Component.literal("     » ")
                .append(Component.literal(currentGen / 4 + " FE/t").withStyle(ChatFormatting.AQUA)));

        tooltip.add(Component.literal("     » Buffer: ")
                .append(Component.literal(energy.getEnergyStored() + " / " + energy.getMaxEnergyStored() + " FE")
                        .withStyle(ChatFormatting.GOLD)));

        return true;
    }
}
