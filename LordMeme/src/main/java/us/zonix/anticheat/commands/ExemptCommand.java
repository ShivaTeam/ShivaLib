package us.zonix.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.data.PlayerData;

@CommandMeta(label = "pussy", permission = "meme.exempt")
public class ExemptCommand {

    private LordMeme plugin = LordMeme.getInstance();

    public void onCommand(CommandSender player, Player target) {
        if(target == null){
            player.sendMessage(ChatColor.RED + "A player with that name could not be found.");
            return;
        }
        final PlayerData targetData = this.plugin.getPlayerDataManager().getPlayerData(target);
        targetData.setBanning(!targetData.isBanning());
        player.sendMessage(ChatColor.GREEN + target.getName() + " is " + (targetData.isBanning() ? "no longer" : "now") + " getting banned by Lord_MeMe.");
    }

}
