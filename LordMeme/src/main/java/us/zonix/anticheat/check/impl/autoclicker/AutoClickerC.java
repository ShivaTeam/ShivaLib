package us.zonix.anticheat.check.impl.autoclicker;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class AutoClickerC extends PacketCheck
{
    private boolean sent;
    
    public AutoClickerC(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Auto-Clicker (Check 3)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockDig) {
            final PacketPlayInBlockDig.EnumPlayerDigType digType = ((PacketPlayInBlockDig)packet).c();
            if (digType == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
                this.sent = true;
            }
            else if (digType == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                int vl = (int)this.getVl();
                if (this.sent) {
                    if (++vl > 10 && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "VL " + vl + ".", false) && !this.playerData.isBanning() && !this.playerData.isRandomBan() && vl >= 20) {
                        this.randomBan(player, 250.0);
                    }
                }
                else {
                    vl = 0;
                }
                this.setVl(vl);
            }
        }
        else if (packet instanceof PacketPlayInArmAnimation) {
            this.sent = false;
        }
    }
}
