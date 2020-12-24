package net.frozenorb.potpvp.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.match.Match;
import net.frozenorb.potpvp.match.MatchHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class MatchListCommand {

    @CommandMeta(label = { "match list" }, permission = "op")
    public static class MatchList{
        public void execute(Player sender) {
            matchList(sender);
        }
    }
    public static void matchList(Player sender) {
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();

        for (Match match : matchHandler.getHostedMatches()) {
            sender.sendMessage(ChatColor.RED + match.getSimpleDescription(true));
        }
    }

}