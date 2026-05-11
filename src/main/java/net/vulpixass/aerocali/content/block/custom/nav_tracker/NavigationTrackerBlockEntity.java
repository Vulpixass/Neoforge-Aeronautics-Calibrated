package net.vulpixass.aerocali.content.block.custom.nav_tracker;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ShaftBlock;
import dev.eriksonn.aeronautics.index.AeroSoundEvents;
import dev.simulated_team.simulated.content.blocks.nav_table.NavTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.item.custom.NavigationElementItem;
import net.vulpixass.aerocali.content.item.data.NavTargetData;
import net.vulpixass.aerocali.data.AerocaliDataComponents;

public class NavigationTrackerBlockEntity extends KineticBlockEntity {
    // Your "RAM" storage
    private int tick = 220;
    private NavTargetData cachedTarget;
    private int redstoneLevel = 0;

    public NavigationTrackerBlockEntity(BlockPos pos, BlockState state) {
        super(AerocaliBlockEntities.TRACKER.get(), pos, state);
    }

    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null)
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return stack.getItem() instanceof NavigationElementItem;
        }
    };

    @Override
    public void tick() {
        super.tick();
        float speed = Math.abs(getSpeed());
        if (level.isClientSide || speed < 30f) {
            if (this.redstoneLevel != 0) {updateRedstone(0);}
            return;
        }

        // Check if the inventory has a compass
        ItemStack compass = inventory.getStackInSlot(0);
        if (!compass.isEmpty()) {
            // 2. Read the data
            NavTargetData data = compass.get(AerocaliDataComponents.NAV_TARGET.get());
            if (data != null) {
                this.cachedTarget = data;
                BlockEntity below = level.getBlockEntity(worldPosition.below());
                if (below instanceof NavTableBlockEntity navTable) {
                    ItemStack heldInTable = navTable.getHeldItem(); // Check their method name!

                    // If the table is empty and we have a compass
                    if (heldInTable.isEmpty() && !inventory.getStackInSlot(0).isEmpty()) {
                        ItemStack toPush = inventory.extractItem(0, 1, false);
                        navTable.setHeldItem(toPush); // Use the method that puts the item in the slot
                    }
                }
            }
        }
        if (this.cachedTarget != null) {
            Vec3 currentPos;

            BlockEntity below = level.getBlockEntity(worldPosition.below());
            if (below instanceof NavTableBlockEntity navTable) {
                currentPos = navTable.getProjectedSelfPos();
            } else {
                currentPos = new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
            }

            if (currentPos == null) {
                currentPos = new Vec3(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
            }

            double dx = currentPos.x - cachedTarget.x();
            double dz = currentPos.z - cachedTarget.z();
            double distance = Math.sqrt(dx * dx + dz * dz);

            int power;
            if (distance <= 2) {
                power = 15;
            } else if (distance > 20) {
                power = 0;
            } else {
                power = (int) (15 - (distance / 20 * 14));
                power = Math.max(1, power);
            }
            updateRedstone(power);
        }
        tick++;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public int getRedstoneLevel() {
        return redstoneLevel;
    }

    private void updateRedstone(int newLevel) {
        if (this.redstoneLevel != newLevel) {
            this.redstoneLevel = newLevel;
            // Trigger a block update so neighbors (redstone dust/wires) react
            level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
            setChanged();
        }
    }

    public boolean isShaftAbove() {
        BlockPos above = this.getBlockPos().above();
        return level.getBlockState(above).getBlock() instanceof ShaftBlock;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("Inventory", inventory.serializeNBT(registries));
        if (cachedTarget != null) {
            compound.putDouble("TargetX", cachedTarget.x());
            compound.putDouble("TargetY", cachedTarget.y());
            compound.putDouble("TargetZ", cachedTarget.z());
        }
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        if (compound.contains("Inventory")) {
            inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
        }
        if (compound.contains("TargetX")) {
            this.cachedTarget = new NavTargetData(
                    compound.getInt("TargetX"),
                    compound.getInt("TargetY"),
                    compound.getInt("TargetZ"),
                    compound.getString("Dimension")
            );
        }
    }
}
