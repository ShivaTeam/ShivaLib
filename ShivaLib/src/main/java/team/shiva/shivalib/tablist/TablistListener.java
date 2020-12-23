package team.shiva.shivalib.tablist;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import team.shiva.shivalib.ShivaLib;

public class TablistListener implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        new BukkitRunnable(){
            public void run() {
                TablistHandler.addPlayer(event.getPlayer());
            }
        }.runTaskLater(ShivaLib.getInstance(), 10L);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        TablistHandler.removePlayer(event.getPlayer());
    }
}
