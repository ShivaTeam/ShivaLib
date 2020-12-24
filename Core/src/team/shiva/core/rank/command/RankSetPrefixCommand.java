package team.shiva.core.rank.command;

import team.shiva.core.rank.Rank;
import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank setprefix", permission = "zoot.admin.rank", async = true)
public class RankSetPrefixCommand {

	public void execute(CommandSender sender, Rank rank, String prefix) {
		if (rank == null) {
			sender.sendMessage(CC.RED + "A rank with that name does not exist.");
			return;
		}

		rank.setPrefix(CC.translate(prefix));
		rank.save();
		rank.refresh();

		sender.sendMessage(CC.GREEN + "You updated the rank's prefix.");
	}

}
