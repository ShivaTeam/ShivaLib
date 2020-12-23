package team.shiva.core.profile.conversation.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

@CommandMeta(label = "say")
public class SayCommand {
    public void execute(Player player, String message){
        player.sendMessage(ChatColor.RED + "/say has been disabled.");
    }
}
