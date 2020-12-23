package team.shiva.shivalib.autoreboot;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import team.shiva.shivalib.ShivaLib;
import team.shiva.shivalib.autoreboot.commands.AutoRebootCommand;
import team.shiva.shivalib.autoreboot.listeners.AutoRebootListener;
import team.shiva.shivalib.autoreboot.tasks.ServerRebootTask;

import java.util.List;
import java.util.concurrent.TimeUnit;

public final class AutoRebootHandler {
    private static List<Integer> rebootTimes;
    private static boolean initiated;
    private static ServerRebootTask serverRebootTask;

    private AutoRebootHandler() {
    }

    public static void init() {
        Preconditions.checkArgument(!initiated, "AutoRebootHandler is already initiated.");
        rebootTimes = ImmutableList.copyOf(ShivaLib.getInstance().getConfig().getIntegerList("AutoRebootTimes"));
        ShivaLib.getInstance().getServer().getPluginManager().registerEvents(new AutoRebootListener(), ShivaLib.getInstance());
        ShivaLib.getInstance().getHoncho().registerCommand(new AutoRebootCommand.Reboot());
        ShivaLib.getInstance().getHoncho().registerCommand(new AutoRebootCommand.RebootCancel());
        initiated = true;
    }

    @Deprecated
    public static void rebootServer(int seconds) {
        AutoRebootHandler.rebootServer(seconds, TimeUnit.SECONDS);
    }

    public static void rebootServer(int timeUnitAmount, TimeUnit timeUnit) {
        if (serverRebootTask != null) {
            throw new IllegalStateException("Reboot already in progress");
        }
        serverRebootTask = new ServerRebootTask(timeUnitAmount, timeUnit);
        serverRebootTask.runTaskTimer(ShivaLib.getInstance(), 20L, 20L);
    }

    public static boolean isRebooting() {
        return serverRebootTask != null;
    }

    public static int getRebootSecondsRemaining() {
        if (serverRebootTask == null) {
            return -1;
        }
        return serverRebootTask.getSecondsRemaining();
    }

    public static void cancelReboot() {
        if (serverRebootTask != null) {
            serverRebootTask.cancel();
            serverRebootTask = null;
        }
    }

    public static List<Integer> getRebootTimes() {
        return rebootTimes;
    }

    public static boolean isInitiated() {
        return initiated;
    }

    static {
        initiated = false;
        serverRebootTask = null;
    }
}

