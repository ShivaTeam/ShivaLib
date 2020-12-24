package us.zonix.anticheat.check.impl.killaura;

import us.zonix.anticheat.check.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;

public class KillAuraN extends AbstractCheck<int[]>
{
    private int doubleSwings;
    private int doubleAttacks;
    private int bareSwings;
    
    public KillAuraN(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, int[].class, "Kill-Aura (Check 14)");
    }
    
    @Override
    public void handleCheck(final Player player, final int[] ints) {
        final int swings = ints[0];
        final int attacks = ints[1];
        if (swings > 1 && attacks == 0) {
            ++this.doubleSwings;
        }
        else if (swings == 1 && attacks == 0) {
            ++this.bareSwings;
        }
        else if (attacks > 1) {
            ++this.doubleAttacks;
        }
        if (this.doubleSwings + this.doubleAttacks == 20) {
            double vl = this.getVl();
            if (this.doubleSwings == 0) {
                if (this.bareSwings > 10 && ++vl > 3.0) {
                    this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "BS " + this.bareSwings + ". VL " + vl + ".", false);
                }
            }
            else {
                --vl;
            }
            this.setVl(vl);
            this.doubleSwings = 0;
            this.doubleAttacks = 0;
            this.bareSwings = 0;
        }
    }
}
