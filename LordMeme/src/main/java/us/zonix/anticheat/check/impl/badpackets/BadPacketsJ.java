package us.zonix.anticheat.check.impl.badpackets;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class BadPacketsJ extends PacketCheck
{
    private boolean placing;
    
    public BadPacketsJ(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Packets (Check 10)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInBlockDig) {
            if (((PacketPlayInBlockDig)packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM) {
                if (!this.placing && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", true)) {
                    final int violations = this.playerData.getViolations(this, 60000L);
                    if (!this.playerData.isBanning() && violations > 2 && !this.playerData.isBanWave()) {
                        this.ban(player);
                    }
                }
                this.placing = false;
            }
        }
        else if (packet instanceof PacketPlayInBlockPlace) {
            this.placing = true;
        }
    }
}
