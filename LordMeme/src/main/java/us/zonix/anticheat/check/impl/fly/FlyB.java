package us.zonix.anticheat.check.impl.fly;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.util.update.*;
import us.zonix.anticheat.event.player.*;

public class FlyB extends PositionCheck
{
    public FlyB(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Flight (Check 2)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        int vl = (int)this.getVl();
        if (!this.playerData.isInLiquid() && !this.playerData.isOnGround()) {
            final double offsetH = Math.hypot(update.getTo().getX() - update.getFrom().getX(), update.getTo().getZ() - update.getFrom().getZ());
            final double offsetY = update.getTo().getY() - update.getFrom().getY();
            if (offsetH > 0.0 && offsetY == 0.0) {
                if (++vl >= 10 && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, String.format("H %.2f. VL %s.", offsetH, vl), true)) {
                    final int violations = this.playerData.getViolations(this, 60000L);
                    if (!this.playerData.isBanning() && violations > 8 && !this.playerData.isBanWave()) {
                        this.ban(player);
                    }
                }
            }
            else {
                vl = 0;
            }
        }
        else {
            vl = 0;
        }
        this.setVl(vl);
    }
}
