package us.zonix.anticheat.check.impl.killaura;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.event.player.*;

public class KillAuraD extends PacketCheck
{
    public KillAuraD(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Kill-Aura (Check 4)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK && this.playerData.isPlacing() && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", true)) {
            final int violations = this.playerData.getViolations(this, 60000L);
            if (!this.playerData.isBanning() && violations > 2 && !this.playerData.isBanWave()) {
                this.ban(player);
            }
        }
    }
}
