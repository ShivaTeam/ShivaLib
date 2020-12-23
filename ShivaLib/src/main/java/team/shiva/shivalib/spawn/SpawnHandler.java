package team.shiva.shivalib.spawn;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import team.shiva.shivalib.ShivaLib;
import team.shiva.shivalib.util.LocationUtils;

import java.io.IOException;

public class SpawnHandler {
    @Getter private static boolean initiated = false;
    @Getter private static Location spawn;
    public static void init(){
        Preconditions.checkArgument(!initiated, "SpawnHandler is already initiated.");
        ShivaLib.getInstance().getServer().getPluginManager().registerEvents(new SpawnListener(), ShivaLib.getInstance());
        ShivaLib.getInstance().getHoncho().registerCommand(new SetSpawnCommand());
        spawn = LocationUtils.deserialize(ShivaLib.getInstance().getMainConfig().getStringOrDefault("Spawn", null));
        if(spawn == null){
            setSpawn(ShivaLib.getInstance().getServer().getWorlds().get(0).getSpawnLocation());
        }
        initiated = true;
    }
    public static void setSpawn(Location location){
        spawn = location;
        spawn.getWorld().setSpawnLocation(spawn.getBlockX(), spawn.getBlockY(), spawn.getBlockZ());
        ShivaLib.getInstance().getMainConfig().getConfiguration().set("Spawn", LocationUtils.serialize(spawn));
        try {
            ShivaLib.getInstance().getMainConfig().getConfiguration().save(ShivaLib.getInstance().getMainConfig().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
