package us.zonix.anticheat.check.impl.aimassist;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.util.update.*;
import us.zonix.anticheat.util.*;
import us.zonix.anticheat.event.player.*;

public class AimAssistB extends RotationCheck
{
    private float suspiciousYaw;
    
    public AimAssistB(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Aim (Check 2)");
    }
    
    @Override
    public void handleCheck(final Player player, final RotationUpdate update) {
        if (System.currentTimeMillis() - this.playerData.getLastAttackPacket() > 10000L) {
            return;
        }
        final float diffYaw = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());
        if (diffYaw > 1.0f && Math.round(diffYaw * 10.0f) * 0.1f == diffYaw && Math.round(diffYaw) != diffYaw && diffYaw % 1.5f != 0.0f) {
            if (diffYaw == this.suspiciousYaw && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, String.format("Y %.1f.", diffYaw), true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 20 && !this.playerData.isBanWave()) {
                    this.ban(player);
                }
            }
            this.suspiciousYaw = Math.round(diffYaw * 10.0f) * 0.1f;
        }
        else {
            this.suspiciousYaw = 0.0f;
        }
    }
}
