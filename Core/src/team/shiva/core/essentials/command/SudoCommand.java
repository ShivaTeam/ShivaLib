package team.shiva.core.essentials.command;

import team.shiva.core.Locale;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "sudo", permission = "zoot.sudo")
public class SudoCommand {

	public void execute(CommandSender sender, Player target, String chat) {
		if (target == null) {
			sender.sendMessage(Locale.PLAYER_NOT_FOUND.format());
			return;
		}

		target.chat(chat);
		sender.sendMessage(ChatColor.GREEN + "Forced target to chat!");
	}

}
