package us.zonix.anticheat.check.impl.badpackets;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.event.player.*;

public class BadPacketsH extends PacketCheck
{
    private int lastSlot;
    
    public BadPacketsH(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Packets (Check 8)");
        this.lastSlot = -1;
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInHeldItemSlot) {
            final int slot = ((PacketPlayInHeldItemSlot)packet).a();
            if (this.lastSlot == slot && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 2 && !this.playerData.isBanWave()) {
                    this.ban(player);
                }
            }
            this.lastSlot = slot;
        }
    }
}
