package team.shiva.core.essentials.command;

import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "more", permission = "zoot.more")
public class MoreCommand {

	public void execute(Player player) {
		if (player.getItemInHand() == null) {
			player.sendMessage(CC.RED + "There is nothing in your hand.");
			return;
		}

		player.getItemInHand().setAmount(64);
		player.updateInventory();
		player.sendMessage(CC.GREEN + "You gave yourself more of the item in your hand.");
	}

}
