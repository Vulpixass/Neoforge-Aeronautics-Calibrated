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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.sound.AerocaliSounds;
import net.vulpixass.aerocali.util.JOMLConversion;

import java.util.List;

public class ThrusterBlockEntity extends BasePropellerBlockEntity implements BlockEntityPropeller {

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
        return 500;
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

        ion = getBlockState().getValue(ThrusterBlock.ION_MODE);

        int rs = getRedstonePower();
        boolean underwater = level.getFluidState(worldPosition).is(FluidTags.WATER);

        int FEperTick = 100;
        if (ion) FEperTick *= 2;

        boolean canRun = rs > 0 && (!underwater || ion);
        float power;

        boolean hasEnergy = energy.getEnergyStored() >= FEperTick;

        if (stallTicks > 0) {
            stallTicks--;
            thrust = 0;
            airflow = 0;
            rotationSpeed = 0; // Okay don't ask why this is here... I extend from the Propeller of Aeronautics so this just comes with it...
            active = false;

            if (!level.isClientSide) {
                BlockState state = getBlockState();
                if (state.getValue(ThrusterBlock.POWERED)) level.setBlock(worldPosition, state.setValue(ThrusterBlock.POWERED, false), 3);
                setChanged();
                sendData();
            }
            return;
        }


        if (!level.isClientSide) {
            BlockState state = getBlockState();
            // Set Thruster to Powered/Unpowered
            if (state.getValue(ThrusterBlock.POWERED) != hasEnergy) {
                level.setBlock(worldPosition, state.setValue(ThrusterBlock.POWERED, hasEnergy), 3);
            }

            // Play Startup and burnout Sounds
            if (hasEnergy && canRun && !active) {
                level.playSound(null, worldPosition, AerocaliSounds.THRUSTER_START.get(), SoundSource.BLOCKS, 0.25f, 0.5f);
                startupTicks = 2;
            } else if ((!hasEnergy || !canRun) && active) {
                level.playSound(null, worldPosition, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.25f, 1f);
            }
        }

        // sets Thrust of the Thruster
        if (canRun && hasEnergy && energy.extractEnergy(FEperTick, true) == FEperTick) {
            energy.extractEnergy(FEperTick, false);
            power = rs / 15f;
            thrust = power * 200f;
            if (ion) thrust *= 2;

            airflow = thrust * 0.8f;
            active = true;

        } else {
            if (canRun) {stallTicks = 80;}
            thrust = 0;
            airflow = 0;
            rotationSpeed = 0;
            active = false;
            return;
        }

        if (thrust > 0) {rotationSpeed = power + 20;}
        else {rotationSpeed = 0;}

        if (!level.isClientSide) {
            setChanged();
            sendData();
        }

        // Damage Players and spawn Fire
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
                        if (level.getBlockState(pos).isAir()) {
                            level.setBlock(pos, fireType.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }
        if (thrust > 0) {
            // makes the No Usage Cooldown of the Thruster work
            if (startupTicks > 0) {
                startupTicks--;
                return;
            }
            // Play the Thrusters Sound
            if (soundCooldown <= 0) {
                if (ion) {level.playSound(null, worldPosition, AerocaliSounds.THRUSTER_ION.get(), SoundSource.BLOCKS
                            , 0.0625f, 1f);
                } else {level.playSound(null, worldPosition, AerocaliSounds.THRUSTER.get(), SoundSource.BLOCKS
                            , 0.0625f, 1f);
                }
                soundCooldown = 15;
            } else {soundCooldown--;}
        }
    }

    // Spawn the Particles of the Thruster
    public void spawnFlameParticles() {
        if (level == null || !level.isClientSide) return;

        Direction out = getBlockDirection();

        ParticleOptions particleType = ion ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME;

        Vec3 base = Vec3.atCenterOf(worldPosition).add(Vec3.atLowerCornerOf(out.getNormal()).scale(0.4));
        int count = (int) Mth.clamp(thrust * 1.2f, 2, 60);

        Vec3 forward = new Vec3(out.getStepX(), out.getStepY(), out.getStepZ()).scale(0.15 + thrust * 0.015);
        for (int i = 0; i < count; i++) {

            double spread = 0.25 + thrust * 0.01;

            double rx = (Math.random() - 0.5) * spread;
            double ry = (Math.random() - 0.5) * spread;
            double rz = (Math.random() - 0.5) * spread;

            Vec3 random = new Vec3(rx, ry, rz);
            Vec3 dir = new Vec3(out.getStepX(), out.getStepY(), out.getStepZ());
            random = random.subtract(dir.scale(random.dot(dir)));

            Vec3 vel = forward.add(random.scale(0.2));
            level.addParticle(particleType, base.x + random.x * 0.2, base.y + random.y * 0.2,
                    base.z + random.z * 0.2, vel.x, vel.y, vel.z);
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
    }

    // Lets the Thruster Read the saved info
    @Override
    public void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        if (tag.contains("Thrust")) thrust = tag.getFloat("Thrust");
        if (tag.contains("Airflow")) airflow = tag.getFloat("Airflow");
        if (tag.contains("Energy")) energy.setEnergy(tag.getInt("Energy"));
    }

    private int getRedstonePower() {
        return level != null ? level.getBestNeighborSignal(worldPosition) : 0;
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

        tooltip.add(Component.literal("     » Stored: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(energy.getEnergyStored() + " / " + energy.getMaxEnergyStored() + " FE")
                        .withStyle(ChatFormatting.GOLD)));

        return true;
    }
}