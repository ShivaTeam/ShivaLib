package us.zonix.anticheat.check.impl.autoclicker;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.check.checks.PacketCheck;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.event.player.PlayerAlertEvent;

public class AutoClickerA extends PacketCheck {
	private int swings;
	private int movements;

	public AutoClickerA(final LordMeme plugin, final PlayerData playerData) {
		super(plugin, playerData, "Auto-Clicker (Check 1)");
	}

	@Override
	public void handleCheck(final Player player, final Packet packet) {
		if (packet instanceof PacketPlayInArmAnimation
		    && !this.playerData.isDigging() && !this.playerData.isPlacing() && !this.playerData.isFakeDigging()
		    && (System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket()) > 220L
		    && (System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp()) < 110L) {
			++this.swings;
		} else if (packet instanceof PacketPlayInFlying && ++this.movements == 20) {
			if (this.swings > 20 &&
			    this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "C " + this.swings + ".", true)) {
				final int violations = this.playerData.getViolations(this, 60000L);
				if (!this.playerData.isBanning() && violations > 3 && !this.playerData.isBanWave()) {
					this.ban(player);
				}
			}

			this.playerData.setLastCps(this.swings);
			this.movements = this.swings = 0;
		}
	}
}
