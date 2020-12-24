package us.zonix.anticheat.check.impl.autoclicker;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class AutoClickerE extends PacketCheck
{
    private boolean failed;
    private boolean sent;
    private int count;
    
    public AutoClickerE(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Auto-Clicker (Check 5)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation
            && (System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket()) > 220L
            && (System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp()) < 110L
            && !this.playerData.isDigging() && !this.playerData.isPlacing() && !this.playerData.isFakeDigging()) {
            if (this.sent) {
                ++this.count;
                if (!this.failed) {
                    int vl = (int)this.getVl();
                    if (++vl >= 5) {
                        this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "CO " + this.count + ".", false);
                        vl = 0;
                    }
                    this.setVl(vl);
                    this.failed = true;
                }
            }
            else {
                this.sent = true;
                this.count = 0;
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            final boolean b = false;
            this.failed = b;
            this.sent = b;
            this.count = 0;
        }
    }
}
