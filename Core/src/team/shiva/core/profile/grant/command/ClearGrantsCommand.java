package team.shiva.core.profile.grant.command;

import team.shiva.core.Core;
import team.shiva.core.Locale;
import team.shiva.core.network.packet.PacketClearGrants;
import team.shiva.core.profile.Profile;
import team.shiva.shivalib.honcho.command.CPL;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "cleargrants", permission = "zoot.admin.cleargrants", async = true)
public class ClearGrantsCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile) {
		if (profile == null) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		profile.getGrants().clear();
		profile.save();

		Core.get().getPidgin().sendPacket(new PacketClearGrants(profile.getUuid()));

		sender.sendMessage(ChatColor.GREEN + "Cleared grants of " + profile.getName() + "!");
	}

}
