package us.zonix.anticheat.check.impl.killaura;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class KillAuraM extends PacketCheck
{
    private int swings;
    private int attacks;
    
    public KillAuraM(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Kill-Aura (Check 13)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (!this.playerData.isDigging() && !this.playerData.isPlacing()) {
            if (packet instanceof PacketPlayInFlying) {
                if (this.attacks > 0 && this.swings > this.attacks) {
                    this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "S " + this.swings + ". A " + this.attacks + ".", false);
                }
                final KillAuraN auraN = this.playerData.getCheck(KillAuraN.class);
                if (auraN != null) {
                    auraN.handleCheck(player, new int[] { this.swings, this.attacks });
                }
                this.swings = 0;
                this.attacks = 0;
            }
            else if (packet instanceof PacketPlayInArmAnimation) {
                ++this.swings;
            }
            else if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                ++this.attacks;
            }
        }
    }
}
