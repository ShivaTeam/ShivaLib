package us.zonix.anticheat.check.impl.autoclicker;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;
import java.util.*;

public class AutoClickerF extends PacketCheck
{
    private final Deque<Integer> recentCounts;
    private BlockPosition lastBlock;
    private int flyingCount;
    
    public AutoClickerF(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Auto-Clicker (Check 6)");
        this.recentCounts = new LinkedList<Integer>();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockDig) {
            final PacketPlayInBlockDig blockDig = (PacketPlayInBlockDig)packet;
            if (blockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                if (this.lastBlock != null && this.lastBlock.equals((Object)blockDig.a())) {
                    double vl = this.getVl();
                    this.recentCounts.addLast(this.flyingCount);
                    if (this.recentCounts.size() == 20) {
                        double average = 0.0;
                        for (final int i : this.recentCounts) {
                            average += i;
                        }
                        average /= this.recentCounts.size();
                        double stdDev = 0.0;
                        for (final int j : this.recentCounts) {
                            stdDev += Math.pow(j - average, 2.0);
                        }
                        stdDev /= this.recentCounts.size();
                        stdDev = Math.sqrt(stdDev);
                        if (stdDev < 0.45 && ++vl >= 3.0) {
                            if (this.alert(PlayerAlertEvent.AlertType.RELEASE, player, String.format("STD %.2f. VL %.1f.", stdDev, vl), false) && !this.playerData.isBanning() && !this.playerData.isRandomBan() && vl >= 6.0) {
                                this.randomBan(player, 200.0);
                            }
                        }
                        else {
                            vl -= 0.5;
                        }
                        this.recentCounts.clear();
                    }
                    this.setVl(vl);
                }
                this.flyingCount = 0;
            }
            else if (blockDig.c() == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                this.lastBlock = blockDig.a();
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            ++this.flyingCount;
        }
    }
}
