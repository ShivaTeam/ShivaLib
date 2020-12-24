package us.zonix.anticheat.check.impl.aimassist;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.util.update.*;
import us.zonix.anticheat.util.*;
import us.zonix.anticheat.event.player.*;

public class AimAssistC extends RotationCheck
{
    public AimAssistC(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Aim (Check 3)");
    }
    
    @Override
    public void handleCheck(final Player player, final RotationUpdate update) {
        if (System.currentTimeMillis() - this.playerData.getLastAttackPacket() > 10000L) {
            return;
        }
        final float diffYaw = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());
        double vl = this.getVl();
        if (update.getFrom().getPitch() == update.getTo().getPitch() && diffYaw >= 3.0f && update.getFrom().getPitch() != 90.0f && update.getTo().getPitch() != 90.0f) {
            if ((vl += 0.9) >= 6.3) {
                this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, String.format("Y %.1f. VL %.1f.", diffYaw, vl), false);
            }
        }
        else {
            vl -= 1.6;
        }
        this.setVl(vl);
    }
}
