package team.shiva.shivalib.util;

import org.bukkit.scheduler.BukkitRunnable;

public class TPSUtils
extends BukkitRunnable {
    private static int TICK_COUNT = 0;
    private static final long[] TICKS = new long[600];

    public static double getTPS() {
        return TPSUtils.getTPS(100);
    }

    public static double getTPS(int ticks) {
        if (TICK_COUNT < ticks) {
            return 20.0;
        }
        int target = (TICK_COUNT - 1 - ticks) % TICKS.length;
        long elapsed = System.currentTimeMillis() - TICKS[target];
        return (double)ticks / ((double)elapsed / 1000.0);
    }

    public void run() {
        TPSUtils.TICKS[TPSUtils.TICK_COUNT % TPSUtils.TICKS.length] = System.currentTimeMillis();
        ++TICK_COUNT;
    }
}

