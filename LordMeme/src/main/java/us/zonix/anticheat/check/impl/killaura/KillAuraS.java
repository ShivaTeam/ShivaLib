package us.zonix.anticheat.check.impl.killaura;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.event.player.*;

public class KillAuraS extends PacketCheck
{
    private boolean sentArmAnimation;
    private boolean sentAttack;
    private boolean sentBlockPlace;
    private boolean sentUseEntity;
    
    public KillAuraS(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Kill-Aura (Check 19)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            this.sentArmAnimation = true;
        }
        else if (packet instanceof PacketPlayInUseEntity) {
            if (((PacketPlayInUseEntity)packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                this.sentAttack = true;
            }
            else {
                this.sentUseEntity = true;
            }
        }
        else if (packet instanceof PacketPlayInBlockPlace && ((PacketPlayInBlockPlace)packet).getItemStack() != null && ((PacketPlayInBlockPlace)packet).getItemStack().getName().toLowerCase().contains("sword")) {
            this.sentBlockPlace = true;
        }
        else if (packet instanceof PacketPlayInFlying) {
            if (this.sentArmAnimation && !this.sentAttack && this.sentBlockPlace && this.sentUseEntity && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 2 && !this.playerData.isBanWave()) {
                    this.ban(player);
                }
            }
            final boolean b = false;
            this.sentUseEntity = b;
            this.sentBlockPlace = b;
            this.sentAttack = b;
            this.sentArmAnimation = b;
        }
    }
}
