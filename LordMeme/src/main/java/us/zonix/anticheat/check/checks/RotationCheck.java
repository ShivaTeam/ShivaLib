package us.zonix.anticheat.check.checks;

import us.zonix.anticheat.check.*;
import us.zonix.anticheat.util.update.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;

public abstract class RotationCheck extends AbstractCheck<RotationUpdate>
{
    public RotationCheck(final LordMeme plugin, final PlayerData playerData, final String name) {
        super(plugin, playerData, RotationUpdate.class, name);
    }
}
