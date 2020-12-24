package team.shiva.core.essentials.command;

import team.shiva.core.CoreAPI;
import team.shiva.core.profile.Profile;
import team.shiva.core.rank.Rank;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

@CommandMeta(label = "list")
public class ListCommand {

    public void executue(Player sender) {
        List<Player> sortedPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        sortedPlayers.sort(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                Profile p1 = Profile.getByUuid(o1.getUniqueId());
                Profile p2 = Profile.getByUuid(o2.getUniqueId());
                return p2.getActiveRank().getWeight() - p1.getActiveRank().getWeight();
            }
        });

        List<String> playerNames = new ArrayList<>();

        for (Player player : sortedPlayers) {
            playerNames.add(CoreAPI.getColoredName(player));
        }

        List<Rank> sortedRanks = new ArrayList<>(Rank.getRanks().values());
        sortedRanks.sort(new Comparator<Rank>() {
            @Override
            public int compare(Rank o1, Rank o2) {
                return o2.getWeight() - o1.getWeight();
            }
        });

        List<String> rankNames = new ArrayList<>();

        for (Rank rank : sortedRanks) {
            rankNames.add(rank.getColor() + rank.getDisplayName());
        }

        sender.sendMessage(StringUtils.join(rankNames, ChatColor.WHITE + ", "));
        sender.sendMessage("(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + "): " +
                           StringUtils.join(playerNames, ChatColor.WHITE + ", "));
    }

}
