package net.vulpixass.aerocali.capabilities;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.vulpixass.aerocali.AeronauticsCalibrated;
import net.vulpixass.aerocali.compat.AerocaliNavTargetCapability;
import net.vulpixass.aerocali.compat.AerocaliNavTargetCapabilityImpl;
import net.vulpixass.aerocali.content.item.AerocaliItems;
import net.vulpixass.aerocali.content.item.custom.NavigationElementItem;

public class AerocaliCapabilities {
    public static final BlockCapability<IEnergyStorage, Void> ENERGY = BlockCapability.create(ResourceLocation
                    .fromNamespaceAndPath("aerocali", "energy"), IEnergyStorage.class, Void.class);
}
