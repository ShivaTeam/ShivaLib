package us.zonix.anticheat.check.impl.badpackets;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class BadPacketsC extends PacketCheck
{
    private boolean sent;
    
    public BadPacketsC(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Packets (Check 3)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInEntityAction) {
            final PacketPlayInEntityAction.EnumPlayerAction playerAction = ((PacketPlayInEntityAction)packet).b();
            if (playerAction == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING || playerAction == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
                if (this.sent) {
                    if (this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", true)) {
                        final int violations = this.playerData.getViolations(this, 60000L);
                        if (!this.playerData.isBanning() && violations > 2 && !this.playerData.isBanWave()) {
                            this.ban(player);
                        }
                    }
                }
                else {
                    this.sent = true;
                }
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }
}
