package us.zonix.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.data.PlayerData;

@CommandMeta(label = "skid", permission = "meme.watch")
public class WatchCommand{

    private LordMeme plugin = LordMeme.getInstance();

    public void onCommand(Player player, Player target) {
        if(target == null){
            player.sendMessage(ChatColor.RED + "A player with that name could not be found.");
            return;
        }
        final PlayerData targetData = this.plugin.getPlayerDataManager().getPlayerData(target);
        targetData.togglePlayerWatching(player);

        player.sendMessage(ChatColor.GREEN + "You are " + (targetData.isPlayerWatching(player) ? "now" : "no longer") + " focusing on " + target.getName() + " anticheat alerts.");
    }

}
