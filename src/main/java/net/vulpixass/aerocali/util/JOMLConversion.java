package net.vulpixass.aerocali.util;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class JOMLConversion {

    public static Vector3d toJOML(Vec3 vec) {
        return new Vector3d(vec.x, vec.y, vec.z);
    }
}
