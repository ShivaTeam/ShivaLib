package us.zonix.anticheat.check.impl.killaura;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.util.*;
import us.zonix.anticheat.event.player.*;

public class KillAuraC extends PacketCheck
{
    private float lastYaw;
    
    public KillAuraC(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Kill-Aura (Check 3)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (this.playerData.getLastTarget() == null) {
            return;
        }
        if (packet instanceof PacketPlayInFlying) {
            final PacketPlayInFlying flying = (PacketPlayInFlying)packet;
            if (flying.h() && !this.playerData.isAllowTeleport()) {
                final CustomLocation targetLocation = this.playerData.getLastPlayerPacket(this.playerData.getLastTarget(), MathUtil.pingFormula(this.playerData.getPing()));
                if (targetLocation == null) {
                    return;
                }
                final CustomLocation playerLocation = this.playerData.getLastMovePacket();
                if (playerLocation.getX() == targetLocation.getX()) {
                    return;
                }
                if (targetLocation.getZ() == playerLocation.getZ()) {
                    return;
                }
                final float yaw = flying.d();
                if (yaw != this.lastYaw) {
                    final float bodyYaw = MathUtil.getDistanceBetweenAngles(yaw, MathUtil.getRotationFromPosition(playerLocation, targetLocation)[0]);
                    if (bodyYaw == 0.0f && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", true)) {
                        final int violations = this.playerData.getViolations(this, 60000L);
                        if (!this.playerData.isBanning() && violations > 5 && !this.playerData.isBanWave()) {
                            this.ban(player);
                        }
                    }
                }
                this.lastYaw = yaw;
            }
        }
    }
}
