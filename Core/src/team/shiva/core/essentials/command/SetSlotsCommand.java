package team.shiva.core.essentials.command;

import team.shiva.core.Core;
import team.shiva.core.util.BukkitReflection;
import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "setslots", async = true, permission = "zoot.setslots")
public class SetSlotsCommand {

	public void execute(CommandSender sender, int slots) {
		BukkitReflection.setMaxPlayers(Core.get().getServer(), slots);
		sender.sendMessage(CC.GOLD + "You set the max slots to " + slots + ".");
	}

}
