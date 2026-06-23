package net.vulpixass.aerocali.util.config;

import net.createmod.catnip.config.ConfigBase;

public class AerocaliConfigCommon extends ConfigBase {
    public final ConfigInt trackerRange;
    public final ConfigInt maxThrust;
    public final ConfigInt maxStored;
    public final ConfigInt transferRate;

    public AerocaliConfigCommon() {
        trackerRange = i(20, 1, 500, "Tracker Range",
                "Maximum distance (in blocks) the Navigation Tracker can detect its target.");
        maxThrust = i(500, 1, 10000, "Maximum Thrust", "Maximum Thrust a Thruster can achieve");
        maxStored = i(100, 1, 10000, "Maximum FE stored", "Maximum amount of FE a cable can store");
        transferRate = i(500, 1, 10000000, "Transfer Rate", "Transfer Rate of the Cables");
    }

    @Override
    public String getName() {
        return "common";
    }
}