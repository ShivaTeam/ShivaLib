package us.zonix.anticheat.check.impl.step;

import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.check.checks.PositionCheck;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.event.player.PlayerAlertEvent;
import us.zonix.anticheat.util.update.PositionUpdate;

public class StepA extends PositionCheck {

	public StepA(final LordMeme plugin, final PlayerData playerData) {
		super(plugin, playerData, "Step (Check 1)");
	}

	@Override
	public void handleCheck(final Player player, final PositionUpdate update) {
		double height = 0.9;
		double difference = update.getTo().getY() - update.getFrom().getY();

		if (difference > height) {
			this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "", true);
		}
	}

}
