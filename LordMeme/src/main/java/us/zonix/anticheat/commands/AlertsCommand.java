package us.zonix.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;
import us.zonix.anticheat.LordMeme;

@CommandMeta(label = {"stfu","alerts"}, permission = "meme.alerts")
public class AlertsCommand {

    private LordMeme plugin = LordMeme.getInstance();

    public void onCommand(Player player) {

        this.plugin.getAlertsManager().toggleAlerts(player);
        player.sendMessage(this.plugin.getAlertsManager().hasAlertsToggled(player) ? (ChatColor.GREEN + "Subscribed to anticheat alerts.") : (ChatColor.RED + "Unsubscribed from anticheat alerts."));

    }

}
