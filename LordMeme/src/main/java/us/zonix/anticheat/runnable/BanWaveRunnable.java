package us.zonix.anticheat.runnable;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.*;
import us.zonix.anticheat.*;
import org.bukkit.command.*;
import org.bukkit.plugin.*;
import us.zonix.anticheat.manager.*;

public class BanWaveRunnable extends BukkitRunnable
{

    private final LordMeme plugin;
    private int countdown;
    
    public void run() {
        final BanWaveManager manager = this.plugin.getBanWaveManager();
        if (manager.isBanWaveStarted()) {
            if (--this.countdown > 0) {
                this.plugin.getServer().broadcastMessage(ChatColor.RED + "(Ban Wave) Starting in " + this.countdown + " second" + (this.countdown == 1 ? "." : "s."));
            }
            else if (this.countdown == 0) {
                this.plugin.getServer().broadcastMessage(ChatColor.RED + "(Ban Wave) The event has begun. Get some popcorn ready!");
                this.countdown = -1;
            }
            else if (manager.getCheaters().size() > 0) {
                final long id = manager.queueCheater();
                final String name = manager.getCheaterName(id);

                this.plugin.getServer().broadcastMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------");
                this.plugin.getServer().broadcastMessage(ChatColor.GOLD + "Lord_MeMe" + ChatColor.RED + " has detected " + ChatColor.GOLD + name + ChatColor.RED + " cheating.");
                this.plugin.getServer().broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "(!) " + ChatColor.RED + "Player has been removed from the network.");
                this.plugin.getServer().broadcastMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------");

                this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, () -> this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), "ban -s " + name + " 30d Unfair Advantage"));

            }
            else if (manager.getCheaters().size() == 0) {
                manager.setBanWaveStarted(false);
            }
        }
        else {

            this.plugin.getServer().broadcastMessage(ChatColor.RED + "(Ban Wave) The event has ended, banning a total of " + manager.getCheaterNames().size() + ".");
            this.plugin.getServer().broadcastMessage(ChatColor.RED + "(Ban Wave) Thanks for watching, with love " + ChatColor.GOLD + "Lord_MeMe" + ChatColor.RED + ". " + "❤");

            this.plugin.getBanWaveManager().clearCheaters();
            this.cancel();
        }
    }
    
    public BanWaveRunnable(final LordMeme plugin) {
        this.countdown = 6;
        this.plugin = plugin;
    }
}
