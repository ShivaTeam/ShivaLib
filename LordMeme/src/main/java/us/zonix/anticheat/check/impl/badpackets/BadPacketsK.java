package us.zonix.anticheat.check.impl.badpackets;

import net.minecraft.server.v1_8_R3.Entity;
import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class BadPacketsK extends PacketCheck
{
    public BadPacketsK(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Packets (Check 11)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInUseEntity) {
            final PacketPlayInUseEntity useEntity = (PacketPlayInUseEntity)packet;
            if (useEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
                final Entity targetEntity = useEntity.a(((CraftPlayer)player).getHandle().getWorld());
                if (targetEntity instanceof EntityPlayer) {
                    final Vec3D vec3D = useEntity.b();
                    if ((Math.abs(vec3D.a) > 0.41 || Math.abs(vec3D.b) > 1.91 || Math.abs(vec3D.c) > 0.41) && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", true)) {
                        final int violations = this.playerData.getViolations(this, 60000L);
                        if (!this.playerData.isBanning() && !this.playerData.isRandomBan() && violations > 2 && !this.playerData.isBanWave()) {
                            this.randomBan(player, 100.0);
                        }
                    }
                }
            }
        }
    }
}
