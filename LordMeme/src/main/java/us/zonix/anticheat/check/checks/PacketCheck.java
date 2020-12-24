package us.zonix.anticheat.check.checks;

import us.zonix.anticheat.check.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;

public abstract class PacketCheck extends AbstractCheck<Packet>
{
    public PacketCheck(final LordMeme plugin, final PlayerData playerData, final String name) {
        super(plugin, playerData, Packet.class, name);
    }
}
