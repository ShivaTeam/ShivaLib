package us.zonix.anticheat.check.impl.autoclicker;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;
import java.util.*;

public class AutoClickerI extends PacketCheck
{
    private final Deque<Integer> recentCounts;
    private int flyingCount;
    
    public AutoClickerI(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Auto-Clicker (Check 9)");
        this.recentCounts = new LinkedList<Integer>();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM) {
            if (this.flyingCount < 10 && this.playerData.getLastAnimationPacket() + 2000L > System.currentTimeMillis()) {
                this.recentCounts.add(this.flyingCount);
                if (this.recentCounts.size() == 100) {
                    double average = 0.0;
                    for (final double flyingCount : this.recentCounts) {
                        average += flyingCount;
                    }
                    average /= this.recentCounts.size();
                    double stdDev = 0.0;
                    for (final long l : this.recentCounts) {
                        stdDev += Math.pow(l - average, 2.0);
                    }
                    stdDev /= this.recentCounts.size();
                    stdDev = Math.sqrt(stdDev);
                    double vl = this.getVl();
                    if (stdDev < 0.2) {
                        if ((vl += 1.4) >= 4.0) {
                            this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, String.format("STD %.2f. VL %.2f.", stdDev, vl), false);
                        }
                    }
                    else {
                        vl -= 0.8;
                    }
                    this.setVl(vl);
                    this.recentCounts.clear();
                }
            }
        }
        else if (packet instanceof PacketPlayInBlockPlace && ((PacketPlayInBlockPlace)packet).getItemStack() != null && ((PacketPlayInBlockPlace)packet).getItemStack().getName().toLowerCase().contains("sword")) {
            this.flyingCount = 0;
        }
        else if (packet instanceof PacketPlayInFlying) {
            ++this.flyingCount;
        }
    }
}
