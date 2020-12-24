package us.zonix.anticheat.check.impl.killaura;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class KillAuraI extends PacketCheck
{
    private boolean sent;
    
    public KillAuraI(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Kill-Aura (Check 9)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockDig && ((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK) {
            if (this.sent) {
                this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "", false);
            }
        }
        else if (packet instanceof PacketPlayInArmAnimation) {
            this.sent = true;
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }
}
