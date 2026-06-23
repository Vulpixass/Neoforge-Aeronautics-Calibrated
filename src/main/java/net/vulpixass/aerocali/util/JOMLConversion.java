package net.vulpixass.aerocali.util;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public class JOMLConversion {
    // Old util class from when Sable didn't work. Still use it cuz why fix it when it ain't broken?
    public static Vector3d toJOML(Vec3 vec) {
        return new Vector3d(vec.x, vec.y, vec.z);
    }
    public static Vec3 toMojang(Vector3dc vec) {return new Vec3(vec.x(), vec.y(), vec.z());}

}
