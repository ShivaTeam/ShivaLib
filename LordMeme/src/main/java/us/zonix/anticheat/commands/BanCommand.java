package us.zonix.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import team.shiva.shivalib.honcho.command.CommandMeta;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.event.player.PlayerBanEvent;

@CommandMeta(label = "rape", permission = "meme.ban")
public class BanCommand {

    private LordMeme plugin = LordMeme.getInstance();

    public void onCommand(CommandSender player, Player target) {
        if(target == null){
            player.sendMessage(ChatColor.RED + "A player with that name could not be found.");
            return;
        }
        final PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(target);
        playerData.setBanning(true);
        final PlayerBanEvent event = new PlayerBanEvent(target, ChatColor.YELLOW + "was banned by " + ChatColor.GOLD + player.getName() + ".");
        this.plugin.getServer().getPluginManager().callEvent((Event)event);
    }

}
