package us.zonix.anticheat.check.impl.fly;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.util.update.*;
import org.bukkit.potion.*;
import us.zonix.anticheat.event.player.*;

public class FlyA extends PositionCheck
{
    public FlyA(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Flight (Check 1)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        int vl = (int)this.getVl();
        if (!this.playerData.isInLiquid() && !this.playerData.isOnGround() && this.playerData.getVelocityV() == 0) {
            if (update.getFrom().getY() >= update.getTo().getY()) {
                return;
            }
            final double distance = update.getTo().getY() - this.playerData.getLastGroundY();
            double limit = 2.0;
            if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                for (final PotionEffect effect : player.getActivePotionEffects()) {
                    if (effect.getType().equals(PotionEffectType.JUMP)) {
                        final int level = effect.getAmplifier() + 1;
                        limit += Math.pow(level + 4.2, 2.0) / 16.0;
                        break;
                    }
                }
            }
            if (distance > limit) {
                if (++vl >= 10 && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "VL " + vl + ".", true)) {
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
