package net.vulpixass.aerocali.content.network;

import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class NetworkDimensionHandler {
    //Creates Network Managers for every Dimension
    private static final Map<Level, NetworkManager> managers = new HashMap<>();

    public static NetworkManager get(Level level) {
        if (level.isClientSide()) return null;
        return managers.computeIfAbsent(level, k -> new NetworkManager());
    }

    public static void unload(Level level) {managers.remove(level);}
}
