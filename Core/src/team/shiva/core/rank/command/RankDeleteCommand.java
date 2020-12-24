package team.shiva.core.rank.command;

import team.shiva.core.Locale;
import team.shiva.core.rank.Rank;
import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank delete", permission = "zoot.admin.rank", async = true)
public class RankDeleteCommand {

	public void execute(CommandSender sender, Rank rank) {
		if (rank == null) {
			sender.sendMessage(Locale.RANK_NOT_FOUND.format());
			return;
		}

		rank.delete();

		sender.sendMessage(CC.GREEN + "You deleted the rank.");
	}

}
