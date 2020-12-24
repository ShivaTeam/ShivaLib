package team.shiva.core.rank.command;

import team.shiva.core.Locale;
import team.shiva.core.rank.Rank;
import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = { "rank removepermission", "rank removeperm", "rank deleteperm", "rank delperm" },
             permission = "zoot.admin.rank",
             async = true)
public class RankRemovePermissionCommand {

	public void execute(CommandSender sender, Rank rank, String permission) {
		if (rank == null) {
			sender.sendMessage(Locale.RANK_NOT_FOUND.format());
			return;
		}

		permission = permission.toLowerCase().trim();

		if (!rank.getPermissions().contains(permission)) {
			sender.sendMessage(CC.RED + "That rank is not assigned that permission.");
		} else {
			rank.getPermissions().remove(permission);
			rank.save();
			rank.refresh();

			sender.sendMessage(CC.GREEN + "Successfully removed permission from rank.");
		}
	}

}
