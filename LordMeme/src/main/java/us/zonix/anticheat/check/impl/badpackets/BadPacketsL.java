package us.zonix.anticheat.check.impl.badpackets;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class BadPacketsL extends PacketCheck
{
    private boolean sent;
    private boolean vehicle;
    
    public BadPacketsL(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Packets (Check 12)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInFlying) {
            if (this.sent) {
                this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "", false);
            }
            final boolean b = false;
            this.vehicle = b;
            this.sent = b;
        }
        else if (packet instanceof PacketPlayInBlockPlace) {
            final PacketPlayInBlockPlace blockPlace = (PacketPlayInBlockPlace)packet;
            if (blockPlace.getFace() == 255) {
                final ItemStack itemStack = blockPlace.getItemStack();
                if (itemStack != null && itemStack.getName().toLowerCase().contains("sword") && this.playerData.isSprinting() && !this.vehicle) {
                    this.sent = true;
                }
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
