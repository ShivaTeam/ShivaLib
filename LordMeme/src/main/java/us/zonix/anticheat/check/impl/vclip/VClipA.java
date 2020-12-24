package us.zonix.anticheat.check.impl.vclip;

import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.check.checks.PositionCheck;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.event.player.PlayerAlertEvent;
import us.zonix.anticheat.util.BlockUtil;
import us.zonix.anticheat.util.update.PositionUpdate;

public class VClipA extends PositionCheck {

    public VClipA(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "V-Clip (Check 1)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        double difference = update.getTo().getY() - update.getFrom().getY();
        if (difference >= 2.0 && !BlockUtil.isBlockFaceAir(player)) {
            player.teleport(update.getFrom());
            this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", true);
        }
    }

}
