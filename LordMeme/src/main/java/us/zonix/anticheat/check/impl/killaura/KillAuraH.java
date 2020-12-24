package us.zonix.anticheat.check.impl.killaura;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class KillAuraH extends PacketCheck
{
    private boolean sent;
    
    public KillAuraH(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Kill-Aura (Check 8)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockDig) {
            final PacketPlayInBlockDig.EnumPlayerDigType digType = ((PacketPlayInBlockDig)packet).c();
            if ((digType == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK || digType == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM) && this.sent && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 2 && !this.playerData.isBanWave()) {
                    this.ban(player);
                }
            }
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            this.sent = true;
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }
}
