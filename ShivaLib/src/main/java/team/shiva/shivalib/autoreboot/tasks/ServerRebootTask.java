package team.shiva.shivalib.autoreboot.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import team.shiva.shivalib.ShivaLib;
import team.shiva.shivalib.util.TimeUtils;

import java.util.concurrent.TimeUnit;

public final class ServerRebootTask
extends BukkitRunnable {
    private int secondsRemaining;
    private boolean wasWhitelisted;

    public ServerRebootTask(int timeUnitAmount, TimeUnit timeUnit) {
        this.secondsRemaining = (int)timeUnit.toSeconds(timeUnitAmount);
        this.wasWhitelisted = ShivaLib.getInstance().getServer().hasWhitelist();
    }

    public void run() {
        if (this.secondsRemaining == 300) {
            ShivaLib.getInstance().getServer().setWhitelist(true);
        } else if (this.secondsRemaining == 0) {
            ShivaLib.getInstance().getServer().setWhitelist(this.wasWhitelisted);
            ShivaLib.getInstance().getServer().shutdown();
        }
        switch (this.secondsRemaining) {
            case 5: 
            case 10: 
            case 15: 
            case 30: 
            case 60: 
            case 120: 
            case 180: 
            case 240: 
            case 300: {
                ShivaLib.getInstance().getServer().broadcastMessage(ChatColor.RED + "\u26a0 " + ChatColor.DARK_RED.toString() + ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0");
                ShivaLib.getInstance().getServer().broadcastMessage(ChatColor.RED + "Server rebooting in " + TimeUtils.formatIntoDetailedString(this.secondsRemaining) + ".");
                ShivaLib.getInstance().getServer().broadcastMessage(ChatColor.RED + "\u26a0 " + ChatColor.DARK_RED.toString() + ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0");
                break;
            }
        }
        --this.secondsRemaining;
    }

    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        Bukkit.setWhitelist(this.wasWhitelisted);
    }

    public int getSecondsRemaining() {
        return this.secondsRemaining;
    }
}

