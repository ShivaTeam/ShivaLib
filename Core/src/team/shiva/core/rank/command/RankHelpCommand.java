package team.shiva.core.rank.command;

import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = { "rank", "rank help" }, permission = "zoot.admin.rank")
public class RankHelpCommand {

	private static final String[][] HELP;

	static {
		HELP = new String[][]{
				new String[]{ "ranks", "List all existing ranks" },
				new String[]{ "rank create <name>", "Create a new rank" },
				new String[]{ "rank delete <rank>", "Delete an existing rank" },
				new String[]{ "rank info <rank>", "Show info about an existing rank" },
				new String[]{ "rank setcolor <rank> <color>", "Set a rank's color" },
				new String[]{ "rank setprefix <rank> <prefix>", "Set a rank's prefix" },
				new String[]{ "rank setweight <rank> <weight>", "Set a rank's weight" },
				new String[]{ "rank addperm <rank> <permission>", "Add a permission to a rank" },
				new String[]{ "rank delperm <rank> <permission>", "Remove a permission from a rank" },
				new String[]{ "rank inherit <parent> <child>", "Make a parent rank inherit a child rank" },
				new String[]{ "rank uninherit <parent> <child>", "Make a parent rank uninherit a child rank" }
		};
	}

	public void execute(CommandSender sender) {
		sender.sendMessage(CC.CHAT_BAR);
		sender.sendMessage(CC.GOLD + "Rank Help");

		for (String[] help : HELP) {
			sender.sendMessage(CC.BLUE + "/" + help[0] + CC.GRAY + " - " + CC.RESET + help[1]);
		}

		sender.sendMessage(CC.CHAT_BAR);
	}

}
