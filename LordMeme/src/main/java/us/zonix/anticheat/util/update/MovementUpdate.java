package us.zonix.anticheat.util.update;

import org.bukkit.entity.*;
import org.bukkit.*;
import net.minecraft.server.v1_8_R3.*;

public class MovementUpdate
{
    private final Player player;
    private final Location to;
    private final Location from;
    private final PacketPlayInFlying packet;
    
    public MovementUpdate(final Player player, final Location to, final Location from, final PacketPlayInFlying packet) {
        this.player = player;
        this.to = to;
        this.from = from;
        this.packet = packet;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public Location getTo() {
        return this.to;
    }
    
    public Location getFrom() {
        return this.from;
    }
    
    public PacketPlayInFlying getPacket() {
        return this.packet;
    }
}
