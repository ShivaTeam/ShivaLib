package team.shiva.shivalib.tablist;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import team.shiva.shivalib.honcho.command.CommandMeta;
import team.shiva.shivalib.tablist.tab.DefaultLayoutProvider;

@CommandMeta(label = {"resetTablist", "resetTab"}, permission = "op")
public class ResetTablistCommand {
    public void execute(CommandSender sender){
        TablistHandler.setLayoutProvider(new DefaultLayoutProvider());
        sender.sendMessage(ChatColor.GREEN + "Reset complete. " + ChatColor.YELLOW + "You can restart the server to undo it.");
    }
}