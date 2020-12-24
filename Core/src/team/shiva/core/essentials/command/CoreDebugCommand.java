package team.shiva.core.essentials.command;

import team.shiva.core.Core;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "core debug", permission = "core.debug")
public class CoreDebugCommand {

	public void execute(CommandSender sender) {
		Core.get().setDebug(!Core.get().isDebug());
		sender.sendMessage("Debug: " + Core.get().isDebug());
	}

}
