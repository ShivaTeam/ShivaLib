package team.shiva.core.rank.command;

import team.shiva.core.Locale;
import team.shiva.core.rank.Rank;
import team.shiva.core.util.CC;
import team.shiva.core.util.TextSplitter;
import team.shiva.shivalib.honcho.command.CommandMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@CommandMeta(label = "rank info", permission = "zoot.admin.rank", async = true)
public class RankInfoCommand {

	public void execute(Player player, Rank rank) {
		if (rank == null) {
			player.sendMessage(Locale.RANK_NOT_FOUND.format());
		} else {
			List<String> toSend = new ArrayList<>();
			toSend.add(CC.CHAT_BAR);
			toSend.add(ChatColor.GOLD + "Rank Information " + ChatColor.GRAY + "(" + ChatColor.RESET +
			           rank.getColor() + rank.getDisplayName() + ChatColor.GRAY + ")");

			toSend.add(ChatColor.GRAY + "Weight: " + ChatColor.RESET + rank.getWeight());
			toSend.add(ChatColor.GRAY + "Prefix: " + ChatColor.RESET + rank.getPrefix() + "Example");

			List<String> permissions = rank.getAllPermissions();

			toSend.add("");
			toSend.add(ChatColor.GRAY + "Permissions: " + ChatColor.RESET + "(" + permissions.size() + ")");

			if (!permissions.isEmpty()) {
				toSend.addAll(TextSplitter.split(46, StringUtils.join(permissions, " "), "", ", "));
			}

			List<Rank> inherited = rank.getInherited();

			toSend.add("");
			toSend.add(ChatColor.GRAY + "Inherits: " + ChatColor.RESET + "(" + inherited.size() + ")");

			if (!rank.getInherited().isEmpty()) {
				List<String> rankNames = rank.getInherited()
				                             .stream()
				                             .map(inheritedRank -> inheritedRank.getColor() + inheritedRank.getDisplayName())
				                             .collect(Collectors.toList());

				toSend.addAll(TextSplitter.split(46, StringUtils.join(rankNames, " "), "", ", "));
			}

			toSend.add(CC.CHAT_BAR);

			for (String line : toSend) {
				player.sendMessage(line);
			}
		}
	}

}
