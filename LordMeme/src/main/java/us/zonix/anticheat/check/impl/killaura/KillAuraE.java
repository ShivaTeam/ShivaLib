package us.zonix.anticheat.check.impl.killaura;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.event.player.*;
import us.zonix.anticheat.util.CustomLocation;

public class KillAuraE extends PacketCheck
{
    private long lastAttack;
    private boolean attack;
    
    public KillAuraE(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Kill-Aura (Check 5)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        double vl = this.getVl();
        if (packet instanceof PacketPlayInUseEntity && ((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && !this.playerData.isAllowTeleport()) {
            final CustomLocation lastMovePacket = this.playerData.getLastMovePacket();
            if (lastMovePacket == null) {
                return;
            }
            final long delay = System.currentTimeMillis() - lastMovePacket.getTimestamp();
            if (delay <= 25.0) {
                this.lastAttack = System.currentTimeMillis();
                this.attack = true;
            }
            else {
                vl -= 0.25;
            }
        }
        else if (packet instanceof PacketPlayInFlying && this.attack) {
            final long time = System.currentTimeMillis() - this.lastAttack;
            if (time >= 25L) {
                if (++vl >= 10.0 && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, String.format("T %s. VL %.2f.", time, vl), false) && !this.playerData.isBanning() && vl >= 20.0 && !this.playerData.isBanWave()) {
                    this.ban(player);
                }
            }
            else {
                vl -= 0.25;
            }
            this.attack = false;
        }
        this.setVl(vl);
    }
}
