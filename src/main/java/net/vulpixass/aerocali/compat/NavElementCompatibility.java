package net.vulpixass.aerocali.compat;

import dev.simulated_team.simulated.service.SimModCompatibilityService;

public class NavElementCompatibility implements SimModCompatibilityService {
    @Override
    public void init() {
        NavElementRegistry.init();
    }

    @Override
    public String getModId() {
        return "aerocali";
    }
}
