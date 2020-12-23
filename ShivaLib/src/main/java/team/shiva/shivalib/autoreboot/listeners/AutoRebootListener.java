package team.shiva.shivalib.autoreboot.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import team.shiva.shivalib.ShivaLib;
import team.shiva.shivalib.autoreboot.AutoRebootHandler;
import team.shiva.shivalib.event.HourEvent;
import team.shiva.shivalib.tablist.TablistHandler;
import team.shiva.shivalib.tablist.tab.StaticTabRow;
import team.shiva.shivalib.tablist.tab.Tab;
import team.shiva.shivalib.tablist.util.TabIcon;
import us.myles.ViaVersion.api.Via;

import java.util.concurrent.TimeUnit;

public class AutoRebootListener
implements Listener {
    @EventHandler
    public void onHour(HourEvent event) {
        if (AutoRebootHandler.getRebootTimes().contains(event.getHour())) {
            AutoRebootHandler.rebootServer(5, TimeUnit.MINUTES);
        }
    }
}

