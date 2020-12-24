package us.zonix.anticheat.check.impl.inventory;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class InventoryE extends PacketCheck
{
    private boolean sent;
    
    public InventoryE(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Inventory (Check 5)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInWindowClick) {
            if (this.sent) {
                this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "", true);
            }
        }
        else if (packet instanceof PacketPlayInClientCommand && ((PacketPlayInClientCommand)packet).a() == PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
            this.sent = true;
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }
}
