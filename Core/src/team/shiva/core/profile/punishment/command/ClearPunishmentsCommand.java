package team.shiva.core.profile.punishment.command;

import team.shiva.core.Core;
import team.shiva.core.Locale;
import team.shiva.core.network.packet.PacketClearPunishments;
import team.shiva.core.profile.Profile;
import team.shiva.shivalib.honcho.command.CPL;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "clearpunishments", permission = "zoot.admin.clearpunishments", async = true)
public class ClearPunishmentsCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile) {
		if (profile == null) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		profile.getPunishments().clear();
		profile.save();

		Core.get().getPidgin().sendPacket(new PacketClearPunishments(profile.getUuid()));

		sender.sendMessage(ChatColor.GREEN + "Cleared punishments of " + profile.getColoredUsername() +
		                   ChatColor.GREEN + "!");
	}

}
