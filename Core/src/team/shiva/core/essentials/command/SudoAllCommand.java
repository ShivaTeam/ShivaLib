package team.shiva.core.essentials.command;

import team.shiva.shivalib.honcho.command.CommandMeta;
import team.shiva.shivalib.honcho.command.CommandOption;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "sudoall", permission = "zoot.sudo", options = "s")
public class SudoAllCommand {

	public void execute(CommandSender sender, CommandOption option, String chat) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (option == null && !player.equals(sender)) {
				continue;
			}

			player.chat(chat);
		}

		sender.sendMessage(ChatColor.GREEN + "Forced all players to chat!");
	}

}
