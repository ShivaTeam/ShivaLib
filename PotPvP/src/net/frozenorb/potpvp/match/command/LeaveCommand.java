package net.frozenorb.potpvp.match.command;

import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameQueue;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.match.Match;
import net.frozenorb.potpvp.match.MatchHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class LeaveCommand {

    @CommandMeta(label = { "spawn", "leave" }, permission = "")
    public static class Leave{
        public void execute(Player sneder){
            leave(sneder);
        }
    }
    public static void leave(Player sender) {
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();

        if (matchHandler.isPlayingMatch(sender)) {
            sender.sendMessage(ChatColor.RED + "You cannot do this while playing in a match.");
            return;
        }

        Game game = GameQueue.INSTANCE.getCurrentGame(sender);
        if (game != null && game.getPlayers().contains(sender)) {
            sender.sendMessage(ChatColor.RED + "You can't do that here.");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Teleporting you to spawn...");

        Match spectating = matchHandler.getMatchSpectating(sender);

        if (spectating == null) {
            PotPvPSI.getInstance().getLobbyHandler().returnToLobby(sender);
        } else {
            spectating.removeSpectator(sender);
        }
    }

}