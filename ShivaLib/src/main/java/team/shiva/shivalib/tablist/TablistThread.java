package team.shiva.shivalib.tablist;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import team.shiva.shivalib.ShivaLib;

public class TablistThread extends Thread{

    Plugin via = ShivaLib.getInstance().getServer().getPluginManager().getPlugin("ViaVersion");

    public TablistThread(){
        this.setName("ShivaLib - Tablist Thread");
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while(via != null){
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
                    TablistHandler.updatePlayer(online);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(250L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
