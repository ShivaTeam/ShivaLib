package us.zonix.anticheat.check.impl.killaura;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class KillAuraL extends PacketCheck
{
    private boolean sent;
    
    public KillAuraL(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Kill-Aura (Check 12)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
            if (this.sent && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 2 && !this.playerData.isBanWave()) {
                    this.ban(player);
                }
            }
        }
        else if (packet instanceof PacketPlayInEntityAction) {
            final PacketPlayInEntityAction.EnumPlayerAction action = ((PacketPlayInEntityAction)packet).b();
            if (action == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING || action == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING || action == PacketPlayInEntityAction.EnumPlayerAction.START_SNEAKING || action == PacketPlayInEntityAction.EnumPlayerAction.STOP_SNEAKING) {
                this.sent = true;
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }
}
