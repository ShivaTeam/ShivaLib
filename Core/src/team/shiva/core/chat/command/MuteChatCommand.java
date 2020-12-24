package team.shiva.core.chat.command;

import team.shiva.core.Core;
import team.shiva.core.Locale;
import team.shiva.core.profile.Profile;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "mutechat", permission = "zoot.staff.mutechat")
public class MuteChatCommand {

	public void execute(CommandSender sender) {
		Core.get().getChat().togglePublicChatMute();

		String senderName;

		if (sender instanceof Player) {
			Profile profile = Profile.getProfiles().get(((Player) sender).getUniqueId());
			senderName = profile.getActiveRank().getColor() + sender.getName();
		} else {
			senderName = ChatColor.DARK_RED + "Console";
		}

		String context = Core.get().getChat().isPublicChatMuted() ? "muted" : "unmuted";

		Bukkit.broadcastMessage(Locale.MUTE_CHAT_BROADCAST.format(context, senderName));
	}

}
