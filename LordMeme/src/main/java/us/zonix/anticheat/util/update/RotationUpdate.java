package us.zonix.anticheat.util.update;

import org.bukkit.entity.*;
import org.bukkit.*;
import net.minecraft.server.v1_8_R3.*;

public class RotationUpdate extends MovementUpdate
{
    public RotationUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packet) {
        super(player, to, from, packet);
    }
}
