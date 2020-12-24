package net.frozenorb.potpvp.morpheus.command;

import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameQueue;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public class ForceEndCommand {

    @CommandMeta(label = { "forceEnd"}, permission = "op")
    public static class ForceEnd{
        public void execute(Player sender){
            forceEnd(sender);
        }
    }
    public static void forceEnd(Player sender) {
        Game game = GameQueue.INSTANCE.getCurrentGame(sender);

        if (game == null) {
            sender.sendMessage("You're not in a game");
            return;
        }

        game.end();
    }

}
