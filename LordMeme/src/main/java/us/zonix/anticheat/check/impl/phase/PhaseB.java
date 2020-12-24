package us.zonix.anticheat.check.impl.phase;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;

public class PhaseB extends PacketCheck
{
    private int stage;
    
    public PhaseB(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Phase (Check 2)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        final String simpleName;
        final String className = simpleName = packet.getClass().getSimpleName();
        switch (simpleName) {
            case "PacketPlayInFlying": {
                if (this.stage == 0) {
                    ++this.stage;
                    break;
                }
                this.stage = 0;
                break;
            }
            case "PacketPlayInEntityAction": {
                if (((PacketPlayInEntityAction)packet).b() == PacketPlayInEntityAction.EnumPlayerAction.START_SNEAKING) {
                    if (this.stage == 1) {
                        ++this.stage;
                        break;
                    }
                    this.stage = 0;
                    break;
                }
                else {
                    if (((PacketPlayInEntityAction)packet).b() == PacketPlayInEntityAction.EnumPlayerAction.START_SNEAKING && this.stage >= 3) {
                        this.plugin.getAlertsManager().forceAlert(player.getName() + " caught using Phase. " + this.stage);
                        break;
                    }
                    break;
                }
            }
            case "PacketPlayInPosition": {
                if (this.stage >= 2) {
                    ++this.stage;
                    break;
                }
                break;
            }
        }
    }
}
