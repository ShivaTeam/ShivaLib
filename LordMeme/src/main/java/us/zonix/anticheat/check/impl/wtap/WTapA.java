package us.zonix.anticheat.check.impl.wtap;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;
import java.util.*;

public class WTapA extends PacketCheck
{
    private final Deque<Integer> recentCounts;
    private boolean release;
    private int flyingCount;
    
    public WTapA(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Tap (Check 1)");
        this.recentCounts = new LinkedList<Integer>();
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInEntityAction) {
            final PacketPlayInEntityAction.EnumPlayerAction playerAction = ((PacketPlayInEntityAction)packet).b();
            if (playerAction == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING) {
                if (this.playerData.getLastAttackPacket() + 1000L > System.currentTimeMillis() && this.flyingCount < 10 && !this.release) {
                    this.recentCounts.add(this.flyingCount);
                    if (this.recentCounts.size() == 20) {
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
                        if (stdDev == 0.0) {
                            if ((vl += 1.2) >= 2.4) {
                                this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, String.format("STD %.2f. VL %.2f.", stdDev, vl), false);
                            }
                        }
                        else {
                            vl -= 2.0;
                        }
                        this.setVl(vl);
                        this.recentCounts.clear();
                    }
                }
            }
            else if (playerAction == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
                this.flyingCount = 0;
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            ++this.flyingCount;
            this.release = false;
        }
        else if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM) {
            this.release = true;
        }
    }
}
