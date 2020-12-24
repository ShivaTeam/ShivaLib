package net.frozenorb.potpvp.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.match.Match;
import net.frozenorb.potpvp.match.MatchHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class MatchStatusCommand {

    @CommandMeta(label = { "match status" }, permission = "")
    public static class MatchStatus{
        public void execute(CommandSender sender, Player target){
            matchStatus(sender, target);
        }
    }
    public static void matchStatus(CommandSender sender, Player target) {
        if(target == null){
            sender.sendMessage(ChatColor.RED + "A player with that name could not be found.");
            return;
        }
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlayingOrSpectating(target);

        if (match == null) {
            sender.sendMessage(ChatColor.RED + target.getName() + " is not playing in or spectating a match.");
            return;
        }

        for (String line : PotPvPSI.getGson().toJson(match).split("\n")) {
            sender.sendMessage("  " + ChatColor.GRAY + line);
        }
    }

}