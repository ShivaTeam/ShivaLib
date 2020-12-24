package us.zonix.anticheat.check.impl.fly;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.util.update.*;
import org.bukkit.potion.*;
import us.zonix.anticheat.event.player.*;

public class FlyC extends PositionCheck
{
    private int illegalMovements;
    private int legalMovements;
    
    public FlyC(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Flight (Check 3)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        if (this.playerData.getVelocityH() == 0) {
            final double offsetH = Math.hypot(update.getTo().getX() - update.getFrom().getX(), update.getTo().getZ() - update.getFrom().getZ());
            int speed = 0;
            for (final PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType().equals((Object)PotionEffectType.SPEED)) {
                    speed = effect.getAmplifier() + 1;
                    break;
                }
            }
            double threshold;
            if (this.playerData.isOnGround()) {
                threshold = 0.34;
                if (this.playerData.isOnStairs()) {
                    threshold = 0.45;
                }
                else if (this.playerData.isOnIce() || this.playerData.getMovementsSinceIce() < 40) {
                    if (this.playerData.isUnderBlock()) {
                        threshold = 1.3;
                    }
                    else {
                        threshold = 0.8;
                    }
                }
                else if (this.playerData.isUnderBlock() || this.playerData.getMovementsSinceUnderBlock() < 40) {
                    threshold = 0.7;
                }
                else if (this.playerData.isOnCarpet()) {
                    threshold = 0.7;
                }
                threshold += 0.06 * speed;
            }
            else {
                threshold = 0.36;
                if (this.playerData.isOnStairs()) {
                    threshold = 0.45;
                }
                else if (this.playerData.isOnIce() || this.playerData.getMovementsSinceIce() < 40) {
                    if (this.playerData.isUnderBlock()) {
                        threshold = 1.3;
                    }
                    else {
                        threshold = 0.8;
                    }
                }
                else if (this.playerData.isUnderBlock() || this.playerData.getMovementsSinceUnderBlock() < 40) {
                    threshold = 0.7;
                }
                else if (this.playerData.isOnCarpet()) {
                    threshold = 0.7;
                }
                threshold += 0.02 * speed;
            }
            threshold += ((player.getWalkSpeed() > 0.2f) ? (player.getWalkSpeed() * 10.0f * 0.33f) : 0.0f);
            if (offsetH > threshold) {
                ++this.illegalMovements;
            }
            else {
                ++this.legalMovements;
            }
            final int total = this.illegalMovements + this.legalMovements;
            if (total == 20) {
                final double percentage = this.illegalMovements / 20.0 * 100.0;
                if (percentage >= 45.0 && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, String.format("P %.1f.", percentage), true)) {
                    final int violations = this.playerData.getViolations(this, 30000L);
                    if (!this.playerData.isBanning() && violations > 5 && !this.playerData.isBanWave()) {
                        this.ban(player);
                    }
                }
                final boolean b = false;
                this.legalMovements = (b ? 1 : 0);
                this.illegalMovements = (b ? 1 : 0);
            }
        }
    }
}
