package team.shiva.core.network.command;

import team.shiva.core.Core;
import team.shiva.core.CoreAPI;
import team.shiva.core.Locale;
import team.shiva.core.network.packet.PacketStaffReport;
import team.shiva.core.profile.Profile;
import team.shiva.core.util.Cooldown;
import team.shiva.shivalib.honcho.command.CPL;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandMeta(label = "report", async = true)
public class ReportCommand {

	public void execute(Player player, @CPL("target") Player target, @CPL("reason") String reason) {
		if (target == null) {
			player.sendMessage(Locale.PLAYER_NOT_FOUND.format());
			return;
		}

		if (player.equals(target)) {
			player.sendMessage(ChatColor.RED + "You cannot report yourself.");
			return;
		}

		Profile profile = Profile.getByUuid(player.getUniqueId());

		if (!profile.getRequestCooldown().hasExpired()) {
			player.sendMessage(ChatColor.RED + "You cannot request assistance that quickly. Try again later.");
			return;
		}

		Core.get().getPidgin().sendPacket(new PacketStaffReport(
				CoreAPI.getColoredName(player),
				CoreAPI.getColoredName(target),
				reason,
				Bukkit.getServerId(),
				Bukkit.getServerName()
		));

		profile.setRequestCooldown(new Cooldown(120_000L));
		player.sendMessage(Locale.STAFF_REQUEST_SUBMITTED.format());
	}

}
