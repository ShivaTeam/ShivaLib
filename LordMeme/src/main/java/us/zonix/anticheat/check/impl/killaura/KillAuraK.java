package us.zonix.anticheat.check.impl.killaura;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.event.player.*;

public class KillAuraK extends PacketCheck
{
    private int ticksSinceStage;
    private int streak;
    private int stage;
    
    public KillAuraK(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Kill-Aura (Check 11)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            if (this.stage == 0) {
                this.stage = 1;
            }
            else {
                final boolean b = false;
                this.stage = (b ? 1 : 0);
                this.streak = (b ? 1 : 0);
            }
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            if (this.stage == 1) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (packet instanceof PacketPlayInFlying.PacketPlayInPositionLook) {
            if (this.stage == 2) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (packet instanceof PacketPlayInFlying.PacketPlayInPosition) {
            if (this.stage == 3) {
                if (++this.streak >= 15) {
                    this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "STR " + this.streak + ".", false);
                }
                this.ticksSinceStage = 0;
            }
            this.stage = 0;
        }
        if (packet instanceof PacketPlayInFlying && ++this.ticksSinceStage > 40) {
            this.streak = 0;
        }
    }
}
