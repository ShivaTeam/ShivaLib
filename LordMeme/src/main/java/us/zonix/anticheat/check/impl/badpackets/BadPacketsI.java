package us.zonix.anticheat.check.impl.badpackets;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class BadPacketsI extends PacketCheck
{
    private float lastYaw;
    private float lastPitch;
    private boolean ignore;
    
    public BadPacketsI(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Packets (Check 9)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying flying = (PacketPlayInFlying)packet;
            if (!flying.g() && flying.h()) {
                if (this.lastYaw == flying.d() && this.lastPitch == flying.e()) {
                    if (!this.ignore) {
                        this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "", false);
                    }
                    this.ignore = false;
                }
                this.lastYaw = flying.d();
                this.lastPitch = flying.e();
            }
            else {
                this.ignore = true;
            }
        }
        else if (packet instanceof PacketPlayInSteerVehicle) {
            this.ignore = true;
        }
    }
}
