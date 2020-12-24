package net.frozenorb.potpvp.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.match.Match;
import net.frozenorb.potpvp.match.MatchTeam;
import team.shiva.shivalib.util.PlayerUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

import java.util.UUID;

public class PingCommand {

    @CommandMeta(label = "ping", permission = "")
    public static class Ping{
        public void execute(Player sender, Player target){
            ping(sender, target);
        }
        public void execute(Player sender){
            ping(sender, sender);
        }
    }
    public static void ping(Player sender, Player target) {
        if(target == null){
            sender.sendMessage(ChatColor.RED + "A player with that name could not be found.");
            return;
        }
        int ping = PlayerUtils.getPing(target);

        sender.sendMessage(target.getDisplayName() + ChatColor.YELLOW + "'s Ping: " + ChatColor.GREEN + ping + "ms");

        if (sender.getName().equalsIgnoreCase(target.getName())) {
            Match match = PotPvPSI.getInstance().getMatchHandler().getMatchPlaying(sender);
            if (match != null) {
                for (MatchTeam team : match.getTeams()) {
                    for (UUID other : team.getAllMembers()) {
                        Player otherPlayer = Bukkit.getPlayer(other);

                        if (otherPlayer != null && !otherPlayer.equals(sender)) {
                            int otherPing = PlayerUtils.getPing(otherPlayer);
                            sender.sendMessage(otherPlayer.getDisplayName() + ChatColor.YELLOW + "'s Ping: " + ChatColor.GREEN + otherPing + "ms");
                        }
                    }
                }
            }
        }
    }

}
