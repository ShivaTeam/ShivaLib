package us.zonix.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.event.BanWaveEvent;

@CommandMeta(label = "banwave", permission = "meme.banwave")
public class BanWaveCommand {

    private LordMeme plugin = LordMeme.getInstance();

    public void onCommand(CommandSender player, String start_end) {

        if(start_end.equalsIgnoreCase("start")) {

            if(this.plugin.getBanWaveManager().isBanWaveStarted()) {
                player.sendMessage(ChatColor.RED + "(Ban Wave) There is currently an active event.");
                return;
            }

            final BanWaveEvent event = new BanWaveEvent(player.getName());
            this.plugin.getServer().getPluginManager().callEvent(event);
            player.sendMessage(ChatColor.GREEN + "(Ban Wave) You just toggled the event to start.");
        }

        else if(start_end.equalsIgnoreCase("end")) {

            if(!this.plugin.getBanWaveManager().isBanWaveStarted()) {
                player.sendMessage(ChatColor.RED + "(Ban Wave) There is no event to end.");
                return;
            }

            this.plugin.getBanWaveManager().setBanWaveStarted(false);
            player.sendMessage(ChatColor.RED + "(Ban Wave) You just toggled the event to end.");
        }
        else{
            player.sendMessage(ChatColor.RED + "Usage: /banwave <start/end>");
            return;
        }

    }

}
