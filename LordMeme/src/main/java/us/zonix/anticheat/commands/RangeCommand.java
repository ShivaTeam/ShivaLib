package us.zonix.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;
import us.zonix.anticheat.LordMeme;

@CommandMeta(label = "setrangevl", permission = "meme.range")
public class RangeCommand {

    private LordMeme plugin = LordMeme.getInstance();

    public void onCommand(CommandSender player, double volume) {

        try {
            this.plugin.setRangeVl(volume);

            player.sendMessage(ChatColor.GREEN + "Range volume has been set to " + volume);
        }
        catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "That's not a correct value.");
        }
    }

}
