package team.shiva.core.rank.command;

import team.shiva.core.rank.Rank;
import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank setweight", permission = "zoot.admin.rank", async = true)
public class RankSetWeightCommand {

	public void execute(CommandSender sender, Rank rank, String weight) {
		if (rank == null) {
			sender.sendMessage(CC.RED + "A rank with that name does not exist.");
			return;
		}

		try {
			Integer.parseInt(weight);
		} catch (Exception e) {
			sender.sendMessage(CC.RED + "Invalid number.");
			return;
		}

		rank.setWeight(Integer.parseInt(weight));
		rank.save();
		rank.refresh();

		sender.sendMessage(CC.GREEN + "You updated the rank's weight.");
	}

}
