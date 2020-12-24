package net.frozenorb.potpvp.follow.command;

import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameQueue;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.match.command.LeaveCommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import team.shiva.shivalib.honcho.command.CommandMeta;

public class SilentFollowCommand {

    @CommandMeta(label = "silentfollow", permission = "potpvp.silent")
    public static class SilentFollow{
        public void execute(Player sender, Player target){
            silentfollow(sender, target);
        }
    }
    public static void silentfollow(Player sender, Player target) {
        sender.setMetadata("ModMode", new FixedMetadataValue(PotPvPSI.getInstance(), true));
        sender.setMetadata("invisible", new FixedMetadataValue(PotPvPSI.getInstance(), true));

        if (PotPvPSI.getInstance().getPartyHandler().hasParty(sender)) {
            LeaveCommand.leave(sender);
        }

        Game game = GameQueue.INSTANCE.getCurrentGame(target);
        if (game != null && game.getPlayers().contains(target)) {
            sender.sendMessage(ChatColor.RED + target.getName() + " is playing an event!");
            return;
        }

        FollowCommand.follow(sender, target);
    }

}
