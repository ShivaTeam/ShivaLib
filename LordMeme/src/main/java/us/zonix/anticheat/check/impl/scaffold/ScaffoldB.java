package us.zonix.anticheat.check.impl.scaffold;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.event.player.*;
import us.zonix.anticheat.util.CustomLocation;

public class ScaffoldB extends PacketCheck
{
    private long lastPlace;
    private boolean place;
    
    public ScaffoldB(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Placement (Check 2)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        double vl = this.getVl();
        if (packet instanceof PacketPlayInBlockPlace && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && !this.playerData.isAllowTeleport()) {
            final CustomLocation lastMovePacket = this.playerData.getLastMovePacket();
            if (lastMovePacket == null) {
                return;
            }
            final long delay = System.currentTimeMillis() - lastMovePacket.getTimestamp();
            if (delay <= 25.0) {
                this.lastPlace = System.currentTimeMillis();
                this.place = true;
            }
            else {
                vl -= 0.25;
            }
        }
        else if (packet instanceof PacketPlayInFlying && this.place) {
            final long time = System.currentTimeMillis() - this.lastPlace;
            if (time >= 25L) {
                if (++vl >= 10.0) {
                    this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, String.format("T %s. VL %.2f.", time, vl), false);
                }
            }
            else {
                vl -= 0.25;
            }
            this.place = false;
        }
        this.setVl(vl);
    }
}
