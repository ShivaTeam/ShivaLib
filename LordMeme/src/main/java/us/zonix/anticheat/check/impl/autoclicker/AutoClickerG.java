package us.zonix.anticheat.check.impl.autoclicker;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class AutoClickerG extends PacketCheck
{
    private boolean failed;
    private boolean sent;
    
    public AutoClickerG(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Auto-Clicker (Check 7)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockPlace && ((PacketPlayInBlockPlace)packet).getFace() == 255 && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && this.playerData.getLastMovePacket() != null && System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L && this.playerData.getLastAnimationPacket() + 1000L > System.currentTimeMillis()) {
            if (this.sent) {
                if (!this.failed) {
                    this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "", false);
                    this.failed = true;
                }
            }
            else {
                this.sent = true;
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            final boolean b = false;
            this.failed = b;
            this.sent = b;
        }
    }
}
