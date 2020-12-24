package us.zonix.anticheat.check.impl.badpackets;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class BadPacketsA extends PacketCheck
{
    private int streak;
    
    public BadPacketsA(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Packets (Check 1)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            if (((PacketPlayInFlying)packet).g()) {
                this.streak = 0;
            }
            else if (++this.streak > 20 && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", false) && !this.playerData.isBanning() && !this.playerData.isBanWave()) {
                this.ban(player);
            }
        }
        else if (packet instanceof PacketPlayInSteerVehicle) {
            this.streak = 0;
        }
    }
}
