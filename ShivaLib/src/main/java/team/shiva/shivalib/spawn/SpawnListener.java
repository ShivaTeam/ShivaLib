package team.shiva.shivalib.spawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import team.shiva.shivalib.ShivaLib;

public class SpawnListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(SpawnHandler.isInitiated()){
            Bukkit.getScheduler().runTaskLater(ShivaLib.getInstance(), new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().teleport(SpawnHandler.getSpawn());
                }
            },1);
        }
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        if(SpawnHandler.isInitiated() && isSpawn(event.getRespawnLocation())){
            event.setRespawnLocation(SpawnHandler.getSpawn());
        }
    }
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event){
        if(SpawnHandler.isInitiated() && isSpawn(event.getTo())){
            event.setTo(SpawnHandler.getSpawn());
        }
    }
    private boolean isSpawn(Location location){
        Location spawn = location.getWorld().getSpawnLocation();
        if(location.equals(spawn)){
            return true;
        }
        spawn.add(0.5, 0, 0.5);
        if(location.equals(spawn)){
            return true;
        }
        return false;
    }
}
