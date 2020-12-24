package net.frozenorb.potpvp.follow.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.follow.FollowHandler;
import net.frozenorb.potpvp.match.Match;
import net.frozenorb.potpvp.match.MatchHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class UnfollowCommand {

    @CommandMeta(label={"unfollow"}, permission="")
    public static class Unfollow{
        public void execute(Player sender){
            unfollow(sender);
        }
    }
    public static void unfollow(Player sender) {
        FollowHandler followHandler = PotPvPSI.getInstance().getFollowHandler();
        MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();

        if (!followHandler.getFollowing(sender).isPresent()) {
            sender.sendMessage(ChatColor.RED + "You're not following anybody.");
            return;
        }

        Match spectating = matchHandler.getMatchSpectating(sender);

        if (spectating != null) {
            spectating.removeSpectator(sender);
        }

        followHandler.stopFollowing(sender);
    }

}