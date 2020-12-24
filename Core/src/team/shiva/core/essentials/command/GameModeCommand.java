package team.shiva.core.essentials.command;

import team.shiva.core.Locale;
import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = { "gamemode", "gm" }, permission = "zoot.gamemode")
public class GameModeCommand {

	public void execute(Player player, GameMode gameMode) {
		if (gameMode == null) {
			player.sendMessage(CC.RED + "That game mode is not valid.");
			return;
		}

		player.setGameMode(gameMode);
		player.updateInventory();
		player.sendMessage(CC.GOLD + "You updated your game mode.");
	}

	public void execute(CommandSender sender, Player target, GameMode gameMode) {
		if (target == null) {
			sender.sendMessage(Locale.PLAYER_NOT_FOUND.format());
			return;
		}

		if (gameMode == null) {
			sender.sendMessage(CC.RED + "That game mode is not valid.");
			return;
		}

		target.setGameMode(gameMode);
		target.updateInventory();
		target.sendMessage(CC.GOLD + "Your game mode has been updated by " + sender.getName());
	}

}
