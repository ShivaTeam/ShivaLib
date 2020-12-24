package net.frozenorb.potpvp.match.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.match.MatchHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class ToggleMatchCommands {

    @CommandMeta(label = { "toggleMatches unranked" }, permission = "op")
    public static class ToggleMatchesUnranked{
        public void execute(Player sender){
            toggleMatchesUnranked(sender);
        }
    }
    public static void toggleMatchesUnranked(Player sender) {
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();

        boolean newState = !matchHandler.isUnrankedMatchesDisabled();
        matchHandler.setUnrankedMatchesDisabled(newState);

        sender.sendMessage(ChatColor.YELLOW + "Unranked matches are now " + ChatColor.UNDERLINE + (newState ? "disabled" : "enabled") + ChatColor.YELLOW + ".");
    }

    @CommandMeta(label = { "toggleMatches ranked" }, permission = "op")
    public static class ToggleMatchesRanked{
        public void execute(Player sender){
            toggleMatchesRanked(sender);
        }
    }
    public static void toggleMatchesRanked(Player sender) {
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();

        boolean newState = !matchHandler.isRankedMatchesDisabled();
        matchHandler.setRankedMatchesDisabled(newState);

        sender.sendMessage(ChatColor.YELLOW + "Ranked matches are now " + ChatColor.UNDERLINE + (newState ? "disabled" : "enabled") + ChatColor.YELLOW + ".");
    }

}