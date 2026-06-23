package net.vulpixass.aerocali.content.block.custom.cable;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.vulpixass.aerocali.content.AerocaliBlockEntities;
import net.vulpixass.aerocali.content.network.NetworkDimensionHandler;
import net.vulpixass.aerocali.content.network.NetworkManager;
import net.vulpixass.aerocali.content.network.cable.EnergyNetwork;
import net.vulpixass.aerocali.util.config.AerocaliAllConfigs;

import java.util.List;
import java.util.UUID;

public class CableBlockEntity extends SmartBlockEntity {
    private final IEnergyStorage networkEnergyProxy = new NetworkEnergyProxy();

    public CableBlockEntity(BlockPos pos, BlockState state) {super(AerocaliBlockEntities.CABLE.get(), pos, state);}
    public IEnergyStorage getEnergyStorage() {return networkEnergyProxy;}

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {}

    @Override
    public void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
    }

    @Override
    public void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
    }

    private class NetworkEnergyProxy implements IEnergyStorage {

        private EnergyNetwork getNetwork() {
            if (level == null || level.isClientSide()) return null;
            NetworkManager manager = NetworkDimensionHandler.get(level);
            if (manager == null) return null;
            UUID networkId = manager.getNetworkUUID(worldPosition);
            if (networkId == null) return null;
            return manager.getNetwork(networkId);
        }

        //Add Energy to the Network
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            EnergyNetwork net = getNetwork();
            if (net == null) return 0;

            int perCableLimit = AerocaliAllConfigs.common.maxStored.get();
            int networkCapacity = net.getBlockList().size() * perCableLimit;

            int currentPower = net.getPower();
            int accepted = Math.min(networkCapacity - currentPower, maxReceive);

            if (!simulate && accepted > 0) {
                net.setPower(currentPower + accepted);
            }
            return accepted;
        }

        //Gets the Maximum Energy a Cable can store
        @Override
        public int getMaxEnergyStored() {
            EnergyNetwork net = getNetwork();
            int perCableLimit = AerocaliAllConfigs.common.maxStored.get();
            return net != null ? (net.getBlockList().size() * perCableLimit) : perCableLimit;
        }

        //Extracts Energy out of the Network
        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            EnergyNetwork net = getNetwork();
            if (net == null) return 0;

            int currentPower = net.getPower();
            int extracted = Math.min(currentPower, maxExtract);

            if (!simulate && extracted > 0) {
                net.setPower(currentPower - extracted);
            }
            return extracted;
        }

        //Gets the Energy of the Network
        @Override
        public int getEnergyStored() {
            EnergyNetwork net = getNetwork();
            return net != null ? net.getPower() : 0;
        }

        @Override
        public boolean canExtract() { return true; }

        @Override
        public boolean canReceive() { return true; }
    }
}
