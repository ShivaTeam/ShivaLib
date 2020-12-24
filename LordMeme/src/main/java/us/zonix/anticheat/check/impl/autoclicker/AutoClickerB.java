package us.zonix.anticheat.check.impl.autoclicker;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.check.checks.PacketCheck;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.event.player.PlayerAlertEvent;

public class AutoClickerB extends PacketCheck {
	private int clicks;
	private int outliers;
	private int flyingCount;
	private boolean release;

	public AutoClickerB(final LordMeme plugin, final PlayerData playerData) {
		super(plugin, playerData, "Auto-Clicker (Check 2)");
	}

	@Override
	public void handleCheck(final Player player, final Packet packet) {
		if (packet instanceof PacketPlayInArmAnimation && !this.playerData.isDigging() &&
		    !this.playerData.isPlacing() &&
		    System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L &&
		    this.playerData.getLastMovePacket() != null &&
		    System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L &&
		    !this.playerData.isFakeDigging()) {
			if (this.flyingCount < 10) {
				if (this.release) {
					this.release = false;
					this.flyingCount = 0;
					return;
				}
				if (this.flyingCount > 3) {
					++this.outliers;
				} else if (this.flyingCount == 0) {
					return;
				}
				if (++this.clicks == 1000) {
					double vl = this.getVl();
					if (this.outliers <= 7) {
						if ((vl += 1.4) >= 4.0) {
							this.alert(PlayerAlertEvent.AlertType.DEVELOPMENT, player,
									String.format("O %s. VL %.2f.", this.outliers, vl), false);
						}
					} else {
						vl -= 0.8;
					}
					this.setVl(vl);
					final boolean b = false;
					this.outliers = (b ? 1 : 0);
					this.clicks = (b ? 1 : 0);
				}
			}
			this.flyingCount = 0;
		} else if (packet instanceof PacketPlayInFlying) {
			++this.flyingCount;
		} else if (packet instanceof PacketPlayInBlockDig &&
		           ((PacketPlayInBlockDig) packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM) {
			this.release = true;
		}
	}
}
