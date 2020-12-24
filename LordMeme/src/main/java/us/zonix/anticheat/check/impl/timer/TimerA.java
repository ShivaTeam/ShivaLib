package us.zonix.anticheat.check.impl.timer;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.event.player.*;
import java.util.*;

public class TimerA extends PacketCheck
{
    private final Deque<Long> delays;
    private long lastPacketTime;
    
    public TimerA(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Timer");
        this.delays = new LinkedList<Long>();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying && !this.playerData.isAllowTeleport() && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L) {
            this.delays.add(System.currentTimeMillis() - this.lastPacketTime);
            if (this.delays.size() == 40) {
                double average = 0.0;
                for (final long l : this.delays) {
                    average += l;
                }
                average /= this.delays.size();
                double vl = this.getVl();
                if (average <= 49.0) {
                    if ((vl += 1.25) >= 4.0 && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, String.format("AVG %.3f. R %.2f. VL %.2f.", average, 50.0 / average, vl), false) && !this.playerData.isBanning() && vl >= 20.0 && !this.playerData.isBanWave()) {
                        this.ban(player);
                    }
                }
                else {
                    vl -= 0.5;
                }
                this.setVl(vl);
                this.delays.clear();
            }
            this.lastPacketTime = System.currentTimeMillis();
        }
    }
}
