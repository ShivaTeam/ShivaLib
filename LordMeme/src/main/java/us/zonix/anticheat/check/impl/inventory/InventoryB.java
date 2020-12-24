package us.zonix.anticheat.check.impl.inventory;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.event.player.*;

public class InventoryB extends PacketCheck
{
    public InventoryB(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Inventory (Check 2)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (((packet instanceof PacketPlayInEntityAction && ((PacketPlayInEntityAction)packet).b() == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING) || packet instanceof PacketPlayInArmAnimation) && this.playerData.isInventoryOpen()) {
            if (this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 5 && !this.playerData.isBanWave()) {
                    this.ban(player);
                }
            }
            this.playerData.setInventoryOpen(false);
        }
    }
}
