package us.zonix.anticheat.check.impl.velocity;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.util.update.*;
import us.zonix.anticheat.util.*;
import us.zonix.anticheat.event.player.*;

public class VelocityA extends PositionCheck
{
    public VelocityA(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Velocity (Check 1)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        int vl = (int)this.getVl();
        if (this.playerData.getVelocityY() > 0.0 && !this.playerData.isUnderBlock() && !this.playerData.isWasUnderBlock() && !this.playerData.isInLiquid() && !this.playerData.isWasInLiquid() && !this.playerData.isInWeb() && !this.playerData.isWasInWeb() && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L) {
            final int threshold = 10 + MathUtil.pingFormula(this.playerData.getPing()) * 2;
            if (++vl >= threshold) {
                if (this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "VL " + vl + ".", true)) {
                    final int violations = this.playerData.getViolations(this, 60000L);
                    if (!this.playerData.isBanning() && !this.playerData.isBanWave() && violations > Math.max(this.playerData.getPing() / 10L, 15L)) {
                        this.ban(player);
                    }
                }
                this.playerData.setVelocityY(0.0);
                vl = 0;
            }
        }
        else {
            vl = 0;
        }
        this.setVl(vl);
    }
}
