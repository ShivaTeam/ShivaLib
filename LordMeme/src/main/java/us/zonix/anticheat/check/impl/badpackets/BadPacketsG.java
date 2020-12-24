package us.zonix.anticheat.check.impl.badpackets;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.event.player.*;

public class BadPacketsG extends PacketCheck
{
    private PacketPlayInEntityAction.EnumPlayerAction lastAction;
    
    public BadPacketsG(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Packets (Check 7)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInEntityAction) {
            final PacketPlayInEntityAction.EnumPlayerAction playerAction = ((PacketPlayInEntityAction)packet).b();
            if (playerAction == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING || playerAction == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
                if (this.lastAction == playerAction && this.playerData.getLastAttackPacket() + 10000L > System.currentTimeMillis() && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", true)) {
                    final int violations = this.playerData.getViolations(this, 60000L);
                    if (!this.playerData.isBanning() && violations > 2 && !this.playerData.isBanWave()) {
                        this.ban(player);
                    }
                }
                this.lastAction = playerAction;
            }
        }
    }
}
