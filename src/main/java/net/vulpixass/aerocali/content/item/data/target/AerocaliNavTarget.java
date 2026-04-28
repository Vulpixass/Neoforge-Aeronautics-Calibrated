package net.vulpixass.aerocali.content.item.data.target;

import com.mojang.serialization.Codec;
import dev.simulated_team.simulated.content.blocks.nav_table.NavTableBlockEntity;
import dev.simulated_team.simulated.content.blocks.nav_table.navigation_target.NavigationTarget;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public record AerocaliNavTarget(double x, double y, double z) implements NavigationTarget {

    public static final Codec<AerocaliNavTarget> CODEC =
            Codec.DOUBLE.listOf().xmap(list -> new AerocaliNavTarget(list.get(0), list.get(1),
                    list.get(2)), t -> List.of(t.x, t.y, t.z));

    public static final StreamCodec<FriendlyByteBuf, AerocaliNavTarget> STREAM_CODEC =
            StreamCodec.of((buf, value) -> {
                buf.writeDouble(value.x);buf.writeDouble(value.y);
                buf.writeDouble(value.z);
                },
                    buf -> new AerocaliNavTarget(buf.readDouble(), buf.readDouble(), buf.readDouble()));

    @Override
    public Vec3 getTarget(NavTableBlockEntity navBE, ItemStack self) {
        return new Vec3(x, y, z);
    }
}


