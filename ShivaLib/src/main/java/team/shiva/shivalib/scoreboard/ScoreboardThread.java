package team.shiva.shivalib.scoreboard;

import org.bukkit.entity.Player;
import team.shiva.shivalib.ShivaLib;

final class ScoreboardThread
extends Thread {
    public ScoreboardThread() {
        super("Shiva - Scoreboard Thread");
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            if(ShivaLib.getInstance() == null){
                continue;
            }
            if(ShivaLib.getInstance().getServer() == null){
                continue;
            }
            if(ShivaLib.getInstance().getServer().getOnlinePlayers() == null){
                continue;
            }
            for (Player online : ShivaLib.getInstance().getServer().getOnlinePlayers()) {
                if(online == null){
                    continue;
                }
                try {
                    ScoreboardHandler.updateScoreboard(online);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(ScoreboardHandler.getUpdateInterval() * 50L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

