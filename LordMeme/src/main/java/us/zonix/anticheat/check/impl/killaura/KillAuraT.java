package us.zonix.anticheat.check.impl.killaura;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;

public class KillAuraT extends PacketCheck
{
    private int entityId;
    private boolean sent;
    
    public KillAuraT(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Kill-Aura (Check 20)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInUseEntity) {
            if (!this.sent) {
                this.sent = true;
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.sent = false;
        }
    }
}
