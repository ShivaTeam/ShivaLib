package us.zonix.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.data.PlayerData;

@CommandMeta(label = "rail", permission = "meme.misplace")
public class MisplaceCommand {

    private LordMeme plugin = LordMeme.getInstance();

    public void onCommand(CommandSender player, Player target, double misplace) {
        if(target == null){
            player.sendMessage(ChatColor.RED + "A player with that name could not be found.");
            return;
        }
        try {
            final PlayerData targetData = this.plugin.getPlayerDataManager().getPlayerData(target);
            targetData.setMisplace(misplace);
            player.sendMessage(ChatColor.GREEN + target.getName() + "''s misplace value set to " + misplace + ".");
        }
        catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "That's not a correct value.");
        }
    }

}
