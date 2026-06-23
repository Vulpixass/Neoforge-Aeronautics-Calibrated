package net.vulpixass.aerocali.content.network.cable;

import net.minecraft.core.BlockPos;

import java.util.Set;
import java.util.UUID;

public class EnergyNetwork {
    int power = 0;
    UUID networkID;
    Set<BlockPos> blockList;

    public EnergyNetwork(UUID networkID, Set<BlockPos> blockList, int power) {
        this.power = power;
        this.networkID = networkID;
        this.blockList = blockList;
    }

    public void setPower(int power) {this.power = power;}
    public void setNetworkID(UUID networkID) {this.networkID = networkID;}
    public void setBlockList(Set<BlockPos> blockList) {this.blockList = blockList;}

    public int getPower() {return power;}
    public UUID getNetworkID() {return networkID;}
    public Set<BlockPos> getBlockList() {return blockList;}

    public void addBlock(BlockPos block) {this.blockList.add(block);}
}
