package team.shiva.core.essentials.command;

import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandMeta(label = { "clearinv", "clear", "ci" }, permission = "zoot.clearinv")
public class ClearCommand {

	public void execute(Player player) {
		player.getInventory().setContents(new ItemStack[36]);
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.updateInventory();
		player.sendMessage(CC.GOLD + "You cleared your inventory.");
	}

	public void execute(CommandSender sender, Player player) {
		player.getInventory().setContents(new ItemStack[36]);
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.updateInventory();
		player.sendMessage(CC.GOLD + "Your inventory has been cleared by " + sender.getName());
	}

}
