package net.frozenorb.potpvp.match.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.arena.Arena;
import net.frozenorb.potpvp.match.Match;
import net.frozenorb.potpvp.match.MatchHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class MapCommand {

    @CommandMeta(label = { "map" }, permission = "")
    public static class Map{
        public void execute(Player sender){
            map(sender);
        }
    }
    public static void map(Player sender) {
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();
        Match match = matchHandler.getMatchPlayingOrSpectating(sender);

        if (match == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a match.");
            return;
        }

        Arena arena = match.getArena();
        sender.sendMessage(ChatColor.YELLOW + "Playing on copy " + ChatColor.GOLD + arena.getCopy() + ChatColor.YELLOW + " of " + ChatColor.GOLD + arena.getSchematic() + ChatColor.YELLOW + ".");
    }

}