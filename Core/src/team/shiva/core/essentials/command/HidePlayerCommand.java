package team.shiva.core.essentials.command;

import team.shiva.core.Locale;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandMeta(label = "hideplayer", permission = "zoot.hideplayer")
public class HidePlayerCommand {

	public void execute(Player player, Player target) {
		if (target == null) {
			player.sendMessage(Locale.PLAYER_NOT_FOUND.format());
			return;
		}

		player.hidePlayer(target);
		player.sendMessage(ChatColor.GOLD + "Hiding " + target.getName() + " from your view");
	}

	public void execute(Player player, Player target1, Player target2) {
		if (target1 == null || target2 == null) {
			player.sendMessage(Locale.PLAYER_NOT_FOUND.format());
			return;
		}

		target1.hidePlayer(target2);
		player.sendMessage(ChatColor.GOLD + "Hiding " + target2.getName() + " from " + target1.getName() + "'s view");
	}

}
