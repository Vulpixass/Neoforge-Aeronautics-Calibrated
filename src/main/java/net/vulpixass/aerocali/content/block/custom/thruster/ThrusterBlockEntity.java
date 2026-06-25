package net.vulpixass.aerocali.content.block.custom.thruster;

import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.eriksonn.aeronautics.content.blocks.propeller.small.BasePropellerBlock;
import dev.eriksonn.aeronautics.content.blocks.propeller.small.BasePropellerBlockEntity;
import dev.ryanhcode.sable.api.block.propeller.BlockEntityPropeller;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.vulpixass.aerocali.content.particle.AerocaliParticles;
import net.vulpixass.aerocali.content.tags.AerocaliTags;
import net.vulpixass.aerocali.util.config.AerocaliAllConfigs;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.sound.AerocaliSounds;
import net.vulpixass.aerocali.util.JOMLConversion;

import java.util.List;

public class ThrusterBlockEntity extends BasePropellerBlockEntity implements BlockEntityPropeller {

    private final FluidTank fuelTank = new FluidTank(1000, fluid -> fluid.is(AerocaliTags.Fluids.THRUSTER_FUEL));

    private final ThrusterEnergy energy = new ThrusterEnergy(5000, 5000, 5000);

    private int soundCooldown = 0;
    private boolean active = false;
    public float thrust = 0;
    private float airflow = 0;
    private int startupTicks = 0;
    private int stallTicks = 0;
    public boolean ion = getBlockState().getValue(ThrusterBlock.ION_MODE);

    public IEnergyStorage getEnergyStorage() {return energy;}

    public ThrusterBlockEntity(BlockPos pos, BlockState state) {
        super(AerocaliBlockEntities.THRUSTER.get(), pos, state);
    }

    @Override
    public void onActiveTick() {
        if (this.prop != null) {this.prop.pushEntities();}
        spawnFlameParticles();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        if (this.prop == null) return;

        this.prop.setThrustDirection(JOMLConversion.toJOML(new Vec3(getBlockDirection().getStepX(),
                getBlockDirection().getStepY(), getBlockDirection().getStepZ()).normalize()));

        this.prop.getLayers().clear();
        this.prop.addSimpleLayer(0, getRadius());
        this.prop.setParticleAmountUpdater(() -> 0.0);
    }

    @Override
    public double getConfigThrust() {
        return AerocaliAllConfigs.common.maxThrust.get();
    }

    @Override
    public double getConfigAirflow() {
        return 400;
    }

    @Override
    public double getCurrentAirPressure() {
        double y = worldPosition.getY();
        double max = 700;

        double p = 1.0 - (y / max);
        return Math.max(p, 0.0);
    }

    @Override
    public double getAirflowScaling() {
        return 1.0;
    }

    @Override
    public void tick() {
        super.tick();

        // Update ion mode
        boolean newIon = getBlockState().getValue(ThrusterBlock.ION_MODE);
        if (newIon != ion) {
            ion = newIon;

            // Switching modes wipes the other storage
            if (ion) {
                fuelTank.setFluid(FluidStack.EMPTY);
            } else {
                energy.setEnergy(0);
            }

            setChanged();
            sendData();
        }

        int rs = getRedstonePower();
        boolean underwater = level.getFluidState(worldPosition).is(FluidTags.WATER);

        // Resource costs
        int FEperTick = ion ? 200 : 0;
        int mBperTick = ion ? 0 : 5;

        // Resource availability
        boolean hasEnergy = ion && energy.getEnergyStored() >= FEperTick;
        boolean hasFuel = !ion && fuelTank.getFluidAmount() >= mBperTick;

        boolean canRun = rs > 0 && (!underwater || ion) && (ion ? hasEnergy : hasFuel);

        float power;

        // Stall handling
        if (stallTicks > 0) {
            stallTicks--;
            thrust = 0;
            airflow = 0;
            rotationSpeed = 0;
            active = false;

            if (!level.isClientSide) {
                BlockState state = getBlockState();
                if (state.getValue(ThrusterBlock.POWERED)) level.setBlock(worldPosition, state.setValue(ThrusterBlock.POWERED, false), 3);
                setChanged();
                sendData();
            }
            return;
        }

        // Server-side powered state + sounds
        if (!level.isClientSide) {
            BlockState state = getBlockState();
            boolean powered = ion ? hasEnergy : hasFuel;

            if (state.getValue(ThrusterBlock.POWERED) != powered) {
                level.setBlock(worldPosition, state.setValue(ThrusterBlock.POWERED, powered), 3);
            }

            if (powered && canRun && !active) {
                level.playSound(null, worldPosition, AerocaliSounds.THRUSTER_START.get(),
                        SoundSource.BLOCKS, 0.25f, 0.5f);
                startupTicks = 2;
            } else if ((!powered || !canRun) && active) {
                level.playSound(null, worldPosition, SoundEvents.FIRE_EXTINGUISH,
                        SoundSource.BLOCKS, 0.25f, 1f);
            }
        }

        // Thrust logic
        if (canRun) {
            // Consume resource
            if (ion) energy.extractEnergy(FEperTick, false);
            else fuelTank.drain(mBperTick, IFluidHandler.FluidAction.EXECUTE);

            power = rs / 15f;
            thrust = (float)(power * getConfigThrust());

            // Ion mode = ×2 thrust
            if (ion) thrust *= 2;

            airflow = thrust * 0.8f;
            active = true;

        } else {
            if (!ion && rs > 0) stallTicks = 80;
            thrust = 0;
            airflow = 0;
            rotationSpeed = 0;
            active = false;
            return;
        }

        // Propeller rotation (pretending to)
        rotationSpeed = thrust > 0 ? power + 20 : 0;

        if (!level.isClientSide) {
            setChanged();
            sendData();
        }

        // Damage + fire
        if (!level.isClientSide && thrust > 0) {
            Direction out = getBlockDirection();
            Vec3 dir = new Vec3(out.getStepX(), out.getStepY(), out.getStepZ()).normalize();

            double coneLength = 3.5 + thrust * 0.15;
            double coneRadius = 0.3 + thrust * 0.01;

            Vec3 start = Vec3.atCenterOf(worldPosition).add(dir.scale(0.6));
            Vec3 end = start.add(dir.scale(coneLength));

            AABB box = new AABB(start, end).inflate(coneRadius);
            List<Entity> entities = level.getEntities(null, box);

            for (Entity e : entities) {
                if (e instanceof LivingEntity living) {
                    Vec3 toEntity = living.position().subtract(start);
                    double dist = toEntity.length();
                    Vec3 norm = toEntity.normalize();
                    if (norm.dot(dir) > 0.82 && dist <= coneLength) {
                        living.hurt(level.damageSources().inFire(), 2.0f);
                        Block fireType = ion ? Blocks.SOUL_FIRE : Blocks.FIRE;
                        BlockPos pos = living.blockPosition();
                        if (level.getBlockState(pos).isAir()) level.setBlock(pos, fireType.defaultBlockState(), 3);
                    }
                }
            }
        }

        // Thruster sound loop
        if (thrust > 0) {
            if (startupTicks > 0) {
                startupTicks--;
                return;
            }

            if (soundCooldown <= 0) {
                if (ion) {
                    level.playSound(null, worldPosition, AerocaliSounds.THRUSTER_ION.get(),
                            SoundSource.BLOCKS, 0.0625f, 1f);
                } else {
                    level.playSound(null, worldPosition, AerocaliSounds.THRUSTER.get(),
                            SoundSource.BLOCKS, 0.0625f, 1f);
                }
                soundCooldown = 15;
            } else soundCooldown--;
        }
    }


    // Spawn the Particles of the Thruster
    public void spawnFlameParticles() {
        if (level == null || !level.isClientSide) return;

        Direction out = getBlockDirection();
        ParticleOptions particleType = ion ? AerocaliParticles.ION_THRUST_PARTICLES.get() : AerocaliParticles.THRUST_PARTICLES.get();
        Vec3 base = Vec3.atCenterOf(worldPosition).add(new Vec3(out.getStepX(), out.getStepY(), out.getStepZ()).scale(1.0));

        int count = (int) Mth.clamp(thrust * 1.2f, 2, 60);
        Vec3 forward = new Vec3(out.getStepX(), out.getStepY(), out.getStepZ()).normalize().scale(0.15 + thrust * 0.015);

        Vec3 dir = forward.normalize();
        Vec3 up = new Vec3(0, 1, 0);

        if (Math.abs(dir.dot(up)) > 0.9) {up = new Vec3(1, 0, 0);}

        Vec3 side = dir.cross(up).normalize();
        Vec3 vertical = dir.cross(side).normalize();

        for (int i = 0; i < count; i++) {
            double offsetAmount = 0.08;
            Vec3 offset = side.scale(offsetAmount);

            double waveSpeed = 0.15;
            double waveSize = 0.03;
            double t = (level.getGameTime() + i * 0.2) * waveSpeed;

            Vec3 wave = side.scale(Math.sin(t) * waveSize).add(vertical.scale(Math.cos(t) * waveSize));
            Vec3 spawnPos = base.add(offset).add(wave);

            level.addParticle(particleType, spawnPos.x, spawnPos.y, spawnPos.z, forward.x, forward.y, forward.z);
        }
    }

    @Override
    protected float getDirectionIndependentSpeed() {
        float base = this.getBlockDirection().getAxisDirection().getStep() * this.rotationSpeed * (10f / 3)
                * (this.getBlockState().getValue(BasePropellerBlock.REVERSED) ? -1 : 1);

        return base;
    }

    @Override
    public double getAirflow() {
        return airflow;
    }

    @Override
    public double getThrust() {
        return thrust;
    }

    @Override
    public boolean isActive() {
        return thrust > 0.01f && getRedstonePower() > 0;
    }

    @Override
    public Direction getBlockDirection() {
        return getBlockState().getValue(ThrusterBlock.FACING);
    }

    public float getRadius() {
        return 1.5f;
    }

    // Saves important info about the Thruster
    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putFloat("Thrust", thrust);
        tag.putFloat("Airflow", airflow);
        tag.putInt("Energy", energy.getEnergyStored());
        if (!fuelTank.getFluid().isEmpty()) tag.put("Fuel", fuelTank.getFluid().save(registries));
    }

    // Lets the Thruster Read the saved info
    @Override
    public void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        if (tag.contains("Thrust")) thrust = tag.getFloat("Thrust");
        if (tag.contains("Airflow")) airflow = tag.getFloat("Airflow");
        if (tag.contains("Energy")) energy.setEnergy(tag.getInt("Energy"));
        if (tag.contains("Fuel")) FluidStack.parse(registries, tag.getCompound("Fuel")).ifPresent(fuelTank::setFluid);
    }

    private int getRedstonePower() {
        return level != null ? level.getBestNeighborSignal(worldPosition) : 0;
    }

    public FluidTank getFuelTank() {
        return fuelTank;
    }

    private static class ThrusterEnergy extends EnergyStorage {
        public ThrusterEnergy(int capacity, int maxReceive, int maxExtract) {
            super(capacity, maxReceive, maxExtract);
        }

        public void setEnergy(int amount) {
            this.energy = Math.min(capacity, Math.max(0, amount));
        }
    }

    // adds the Goggle Tooltips... thats it...
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        tooltip.add(Component.literal("    Thruster Stats:").withStyle(ChatFormatting.WHITE));

        if (ion) {
            tooltip.add(Component.literal("     » FE: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(energy.getEnergyStored() + " / " + energy.getMaxEnergyStored() + " FE").withStyle(ChatFormatting.GOLD)));
        } else {
            tooltip.add(Component.literal("     » Fuel: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(fuelTank.getFluidAmount() + " / " + fuelTank.getCapacity() + "mb").withStyle(ChatFormatting.AQUA)));
        }
        return true;
    }
}