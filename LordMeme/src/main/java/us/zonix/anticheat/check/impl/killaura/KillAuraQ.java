package us.zonix.anticheat.check.impl.killaura;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class KillAuraQ extends PacketCheck
{
    private boolean sentAttack;
    private boolean sentInteract;
    
    public KillAuraQ(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Kill-Aura (Check 17)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockPlace) {
            if (this.sentAttack && !this.sentInteract && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 2 && !this.playerData.isBanWave()) {
                    this.ban(player);
                }
            }
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            final PacketPlayInUseEntity.EnumEntityUseAction action = ((PacketPlayInUseEntity)packet).a();
            if (action == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                this.sentAttack = true;
            }
            else if (action == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) {
                this.sentInteract = true;
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            final boolean b = false;
            this.sentInteract = b;
            this.sentAttack = b;
        }
    }
}
