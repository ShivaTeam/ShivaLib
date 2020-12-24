package us.zonix.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;
import us.zonix.anticheat.LordMeme;

@CommandMeta(label = {"gtfo","devalerts"} , permission = "meme.devalerts")
public class DevAlertsCommand {

    private LordMeme plugin = LordMeme.getInstance();

    public void onCommand(Player player) {
        this.plugin.getAlertsManager().toggleDevAlerts(player);
        player.sendMessage(this.plugin.getAlertsManager().hasDevAlertsToggled(player) ? (ChatColor.GREEN + "Subscribed to anticheat dev alerts.") : (ChatColor.RED + "Unsubscribed from anticheat dev alerts."));

    }

}
