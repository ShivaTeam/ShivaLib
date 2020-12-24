package us.zonix.anticheat.check.impl.killaura;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.check.checks.PacketCheck;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.event.player.PlayerAlertEvent;

public class KillAuraB extends PacketCheck {
	private boolean sent;
	private boolean failed;
	private int movements;

	public KillAuraB(final LordMeme plugin, final PlayerData playerData) {
		super(plugin, playerData, "Kill-Aura (Check 2)");
	}

	@Override
	public void handleCheck(final Player player, final Packet packet) {
		if (this.playerData.isDigging() && !this.playerData.isInstantBreakDigging() &&
		    System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L &&
		    this.playerData.getLastMovePacket() != null &&
		    System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L) {
			int vl = (int) this.getVl();
			if (packet instanceof PacketPlayInBlockDig &&
			    ((PacketPlayInBlockDig) packet).c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
				this.movements = 0;
				vl = 0;
			} else if (packet instanceof PacketPlayInArmAnimation && this.movements >= 2) {
				if (this.sent) {
					if (!this.failed) {
						if (++vl >= 5) {
							this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "VL " + vl + ".", false);
						}
						this.failed = true;
					}
				} else {
					this.sent = true;
				}
			} else if (packet instanceof PacketPlayInFlying) {
				final boolean b = false;
				this.failed = b;
				this.sent = b;
				++this.movements;
			}
			this.setVl(vl);
		}
	}
}
