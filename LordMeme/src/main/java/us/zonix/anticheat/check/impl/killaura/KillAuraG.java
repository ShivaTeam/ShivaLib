package us.zonix.anticheat.check.impl.killaura;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.event.player.*;

public class KillAuraG extends PacketCheck
{
    private int stage;
    
    public KillAuraG(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Kill-Aura (Check 7)");
        this.stage = 0;
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        final int calculusStage = this.stage % 6;
        if (calculusStage == 0) {
            if (packet instanceof PacketPlayInArmAnimation) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (calculusStage == 1) {
            if (packet instanceof PacketPlayInUseEntity) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (calculusStage == 2) {
            if (packet instanceof PacketPlayInEntityAction) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (calculusStage == 3) {
            if (packet instanceof PacketPlayInFlying) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (calculusStage == 4) {
            if (packet instanceof PacketPlayInEntityAction) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (calculusStage == 5) {
            if (packet instanceof PacketPlayInFlying) {
                if (++this.stage >= 30 && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "S " + this.stage + ".", true)) {
                    final int violations = this.playerData.getViolations(this, 60000L);
                    if (!this.playerData.isBanning() && violations > 5 && !this.playerData.isBanWave()) {
                        this.ban(player);
                    }
                }
            }
            else {
                this.stage = 0;
            }
        }
    }
}
