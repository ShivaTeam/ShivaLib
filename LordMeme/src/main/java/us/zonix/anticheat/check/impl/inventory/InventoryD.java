package us.zonix.anticheat.check.impl.inventory;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.event.player.*;

public class InventoryD extends PacketCheck
{
    private int stage;
    
    public InventoryD(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Inventory (Check 4)");
        this.stage = 0;
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (this.stage == 0) {
            if (packet instanceof PacketPlayInClientCommand && ((PacketPlayInClientCommand)packet).a() == PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
                ++this.stage;
            }
        }
        else if (this.stage == 1) {
            if (packet instanceof PacketPlayInFlying.PacketPlayInLook) {
                ++this.stage;
            }
            else {
                this.stage = 0;
            }
        }
        else if (this.stage == 2) {
            if (packet instanceof PacketPlayInFlying.PacketPlayInLook) {
                this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "", false);
            }
            this.stage = 0;
        }
    }
}
