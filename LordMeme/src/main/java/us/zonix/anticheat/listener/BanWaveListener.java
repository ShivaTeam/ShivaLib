package us.zonix.anticheat.listener;

import org.bukkit.ChatColor;
import us.zonix.anticheat.*;
import us.zonix.anticheat.event.*;
import us.zonix.anticheat.runnable.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;

public class BanWaveListener implements Listener
{
    private final LordMeme plugin;
    
    @EventHandler
    public void onBanWave(final BanWaveEvent e) {
        this.plugin.getLogger().info(ChatColor.RED + "(Ban Wave) Started & executed by " + ((e.getInstigator() == null) ? "CONSOLE" : e.getInstigator()) + ".");
        this.plugin.getBanWaveManager().setBanWaveStarted(true);
        this.plugin.getBanWaveManager().loadCheaters();
        new BanWaveRunnable(this.plugin).runTaskTimerAsynchronously((Plugin)this.plugin, 20L, 20L);
    }
    
    public BanWaveListener(final LordMeme plugin) {
        this.plugin = plugin;
    }
}
