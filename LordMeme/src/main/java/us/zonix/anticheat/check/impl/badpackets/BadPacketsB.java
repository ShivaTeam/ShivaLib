package us.zonix.anticheat.check.impl.badpackets;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.event.player.*;

public class BadPacketsB extends PacketCheck
{
    public BadPacketsB(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Packets (Check 2)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying && Math.abs(((PacketPlayInFlying)packet).e()) > 90.0f && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", false) && !this.playerData.isBanning() && !this.playerData.isRandomBan()) {
            this.randomBan(player, 200.0);
        }
    }
}
