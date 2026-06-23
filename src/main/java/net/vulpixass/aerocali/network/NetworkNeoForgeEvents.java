package net.vulpixass.aerocali.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.vulpixass.aerocali.content.block.custom.cable.CableBlock;
import net.vulpixass.aerocali.content.network.NetworkDimensionHandler;
import net.vulpixass.aerocali.content.network.NetworkManager;
import net.vulpixass.aerocali.content.network.cable.EnergyNetwork;
import net.vulpixass.aerocali.util.config.AerocaliAllConfigs;

import java.util.UUID;

@EventBusSubscriber(modid = "aerocali")
public class NetworkNeoForgeEvents {
    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        // Clean up memory when the server stops or a dimension unloads the cable
        NetworkDimensionHandler.unload((Level) event.getLevel());
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (Level level : event.getServer().getAllLevels()) {
            NetworkManager manager = NetworkDimensionHandler.get(level);
            if (manager == null) continue;

            // Loop through every single tracked cable block pos
            for (BlockPos cablePos : manager.getBlockToNetworkMap().keySet()) {
                UUID networkId = manager.getNetworkUUID(cablePos);
                EnergyNetwork net = manager.getNetwork(networkId);
                if (net == null) continue;

                //  Force the blockstate to match the network energy
                BlockState state = level.getBlockState(cablePos);
                if (state.getBlock() instanceof CableBlock && state.hasProperty(CableBlock.ACTIVE)) {
                    boolean shouldBeActive = net.getPower() > 0;
                    boolean isActiveNow = state.getValue(CableBlock.ACTIVE);

                    if (shouldBeActive != isActiveNow) {
                        // Update the blockstate flag cleanly without interrupting block placement
                        level.setBlock(cablePos, state.setValue(CableBlock.ACTIVE, shouldBeActive), 3);
                    }
                }

                // Scan all 6 sides of this specific cable
                for (Direction dir : Direction.values()) {
                    BlockPos neighborPos = cablePos.relative(dir);
                    if (level.getBlockState(neighborPos).getBlock() instanceof CableBlock) continue;
                    IEnergyStorage targetStorage = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, dir.getOpposite());
                    if (targetStorage == null) continue;

                    // pull Energy from Energy Sources
                    if (targetStorage.canExtract()) {
                        int perCableLimit = AerocaliAllConfigs.common.maxStored.get();
                        int networkCapacity = net.getBlockList().size() * perCableLimit;

                        int spaceLeft = networkCapacity - net.getPower();

                        if (spaceLeft > 0) {
                            int maxPull = Math.min(spaceLeft, AerocaliAllConfigs.common.transferRate.get());
                            int simulatedExtract = targetStorage.extractEnergy(maxPull, true);

                            if (simulatedExtract > 0) {
                                int actualExtracted = targetStorage.extractEnergy(simulatedExtract, false);
                                net.setPower(net.getPower() + actualExtracted);
                            }
                        }
                    }

                    // Push Energy to consumers
                    if (net.getPower() > 0 && targetStorage.canReceive()) {
                        int maxPush = Math.min(net.getPower(), AerocaliAllConfigs.common.transferRate.get());
                        int simulatedReceive = targetStorage.receiveEnergy(maxPush, true);

                        if (simulatedReceive > 0) {
                            int actualReceived = targetStorage.receiveEnergy(simulatedReceive, false);
                            net.setPower(net.getPower() - actualReceived);
                        }
                    }
                }
            }
        }
    }
}
