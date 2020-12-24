package us.zonix.anticheat.check.impl.autoclicker;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.check.checks.PacketCheck;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.event.player.PlayerAlertEvent;

public class AutoClickerJ extends PacketCheck {
	private int stage;

	public AutoClickerJ(final LordMeme plugin, final PlayerData playerData) {
		super(plugin, playerData, "Auto-Clicker (Check 10)");
	}

	@Override
	public void handleCheck(final Player player, final Packet packet) {
		if (this.stage == 0) {
			if (packet instanceof PacketPlayInArmAnimation) {
				++this.stage;
			}
		} else if (packet instanceof PacketPlayInBlockDig) {
			if (this.playerData.getFakeBlocks().contains(((PacketPlayInBlockDig) packet).a())) {
				return;
			}

			double vl = this.getVl();

			PacketPlayInBlockDig.EnumPlayerDigType digType = ((PacketPlayInBlockDig) packet).c();
			if (digType == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
				if (this.stage == 1) {
					++this.stage;
				} else {
					this.stage = 0;
				}
			} else if (digType == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
				if (this.stage == 2) {
					if ((vl += 1.4) >= 15.0 &&
					    this.alert(PlayerAlertEvent.AlertType.RELEASE, player, String.format("VL %.2f.", vl), true) &&
					    !this.playerData.isBanning() && !this.playerData.isRandomBan() && vl >= 50.0) {
						this.randomBan(player, 250.0);
					}
				} else {
					this.stage = 0;
					vl -= 0.25;
				}
			} else {
				this.stage = 0;
			}
			this.setVl(vl);
		} else {
			this.stage = 0;
		}
	}
}
