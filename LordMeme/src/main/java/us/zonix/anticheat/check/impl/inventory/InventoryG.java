package us.zonix.anticheat.check.impl.inventory;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class InventoryG extends PacketCheck
{
    private boolean sent;
    private boolean vehicle;
    
    public InventoryG(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Inventory (Check 7)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            if (this.sent) {
                this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "", true);
            }
            final boolean b = false;
            this.vehicle = b;
            this.sent = b;
        }
        else if (packet instanceof PacketPlayInClientCommand && ((PacketPlayInClientCommand)packet).a() == PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
            if (this.playerData.isSprinting() && !this.vehicle) {
                this.sent = true;
            }
        }
        else if (packet instanceof PacketPlayInEntityAction && ((PacketPlayInEntityAction)packet).b() == PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING) {
            this.sent = false;
        }
        else if (packet instanceof PacketPlayInSteerVehicle) {
            this.vehicle = true;
        }
    }
}
