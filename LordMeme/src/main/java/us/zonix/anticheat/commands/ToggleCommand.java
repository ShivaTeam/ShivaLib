package us.zonix.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;
import us.zonix.anticheat.LordMeme;

import java.util.HashSet;
import java.util.Set;

@CommandMeta(label = "kys", permission = "meme.toggle")
public class ToggleCommand {

	public static final Set<String> DISABLED_CHECKS;
	private LordMeme plugin = LordMeme.getInstance();

	public void onCommand(CommandSender player, String checkName) {

		final String check = checkName.toUpperCase();

		if (!ToggleCommand.DISABLED_CHECKS.remove(check)) {
			ToggleCommand.DISABLED_CHECKS.add(check);
			player.sendMessage(ChatColor.RED + check + " has been disabled.");
		} else {
			player.sendMessage(ChatColor.GREEN + check + " has been enabled.");
		}
	}

	static {
		DISABLED_CHECKS = new HashSet<String>();
	}

}
