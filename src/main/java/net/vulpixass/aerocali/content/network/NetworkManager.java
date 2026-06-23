package net.vulpixass.aerocali.content.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.vulpixass.aerocali.content.network.cable.EnergyNetwork;

import java.util.*;

public class NetworkManager {
    private final Map<UUID, EnergyNetwork> networks = new HashMap<>();
    private final Map<BlockPos, UUID> blockToNetworkMap = new HashMap<>();

    public NetworkManager() {}

    public EnergyNetwork createNetwork (UUID id, Set<BlockPos> initializeBlocks, int initialPower) {
        EnergyNetwork net = new EnergyNetwork(id, initializeBlocks, initialPower);
        networks.put(id, net);

        for (BlockPos pos : initializeBlocks) {blockToNetworkMap.put(pos, id);}
        return net;
    }

    //Get the Network of a Block
    public EnergyNetwork getNetwork (UUID networkId) {return networks.get(networkId);}
    //Get the NetworkID from a block
    public UUID getNetworkUUID (BlockPos block) {return blockToNetworkMap.get(block);}

    public void deleteNetwork (UUID networkId) {
        EnergyNetwork net = networks.remove(networkId);
        if (net != null) for (BlockPos pos : net.getBlockList()) {blockToNetworkMap.remove(pos);}
    }

    public void mergeNetworks(Set<UUID> networkIDs) {
        if (networkIDs == null || networkIDs.size() <= 1) return;

        List<UUID> idList = new ArrayList<>(networkIDs);

        //Gets the main Network (first added Network)
        UUID masterID = idList.get(0);
        EnergyNetwork masterNetwork = getNetwork(masterID);
        if (masterNetwork == null) return;

        //For all connected Networks:
        for (int i = 1; i < idList.size(); i++) {
            UUID currentID = idList.get(i);
            EnergyNetwork currentNetwork = getNetwork(currentID);

            if (currentNetwork == null || currentID.equals(masterID)) continue;

            //Set the Networks FE
            masterNetwork.setPower(masterNetwork.getPower() + currentNetwork.getPower());

            //Add the Networks BLocks to the Master Network
            for (BlockPos pos : currentNetwork.getBlockList()) {
                masterNetwork.addBlock(pos);
                blockToNetworkMap.put(pos, masterID);
            }

            //Remove the old Network
            networks.remove(currentID);
        }
    }

    public void addBlockToNetwork(BlockPos pos, UUID networkId) {
        EnergyNetwork net = getNetwork(networkId);
        if (net != null) {
            net.addBlock(pos);
            blockToNetworkMap.put(pos, networkId);
        }
    }

    public void splitNetwork(BlockPos removedPos, UUID oldNetworkId) {
        //Get the current unchanged Network (UN)
        EnergyNetwork oldNetwork = getNetwork(oldNetworkId);
        if (oldNetwork == null) return;

        //Remove the broken block from the UN
        blockToNetworkMap.remove(removedPos);
        oldNetwork.getBlockList().remove(removedPos);

        if (oldNetwork.getBlockList().isEmpty()) {
            deleteNetwork(oldNetworkId);
            return;
        }

        Set<BlockPos> remainingBlocks = new HashSet<>(oldNetwork.getBlockList());
        int totalPower = oldNetwork.getPower();

        int totalBlocksCount = remainingBlocks.size() + 1;

        deleteNetwork(oldNetworkId);

        // Keep looping until every single block in the set has been assigned to a fragment (Breadth-First Search)
        while (!remainingBlocks.isEmpty()) {
            BlockPos start = remainingBlocks.iterator().next();

            Set<BlockPos> connectedFragment = new HashSet<>();
            Queue<BlockPos> queue = new LinkedList<>();

            queue.add(start);

            while (!queue.isEmpty()) {
                BlockPos current = queue.poll();
                if (connectedFragment.contains(current)) continue;

                connectedFragment.add(current);
                remainingBlocks.remove(current);

                for (Direction dir : Direction.values()) {
                    BlockPos neighbor = current.relative(dir);
                    if (remainingBlocks.contains(neighbor)) queue.add(neighbor);
                }
            }

            //Calculate the new Power (if the Network was split in 2: divide Power by 2 said EXTREMELY simple)
            int proportionalPower = (int) (((double) connectedFragment.size() / totalBlocksCount) * totalPower);
            createNetwork(UUID.randomUUID(), connectedFragment, proportionalPower);
        }
    }

    public Map<BlockPos, UUID> getBlockToNetworkMap() {return blockToNetworkMap;}
}
