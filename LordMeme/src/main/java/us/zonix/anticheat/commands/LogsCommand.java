package us.zonix.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import team.shiva.shivalib.honcho.command.CommandMeta;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.log.LogRequestFecther;

@CommandMeta(label = "logs", permission = "meme.logs")
public class LogsCommand {

    public void onCommand(CommandSender sender, String player) {
        if(player == null){
            sender.sendMessage(ChatColor.RED + "A player with that name could not be found.");
            return;
        }
        new BukkitRunnable() {
            public void run() {
                new LogRequestFecther(sender, player);
            }
        }.runTaskAsynchronously(LordMeme.getInstance());
    }

}
