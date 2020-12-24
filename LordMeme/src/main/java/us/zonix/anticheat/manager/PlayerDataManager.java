package us.zonix.anticheat.manager;

import us.zonix.anticheat.data.*;
import us.zonix.anticheat.*;
import org.bukkit.entity.*;
import java.util.*;

public class PlayerDataManager
{
    private final Map<UUID, PlayerData> playerDataMap;
    private final LordMeme plugin;
    
    public void addPlayerData(final Player player) {
        this.playerDataMap.put(player.getUniqueId(), new PlayerData(this.plugin));
    }
    
    public void removePlayerData(final Player player) {
        this.playerDataMap.remove(player.getUniqueId());
    }

    public boolean hasPlayerData(final Player player) {
        return this.playerDataMap.containsKey(player.getUniqueId());
    }

    public PlayerData getPlayerData(final Player player) {
        return this.playerDataMap.get(player.getUniqueId());
    }
    
    public PlayerDataManager(final LordMeme plugin) {
        this.playerDataMap = new HashMap<UUID, PlayerData>();
        this.plugin = plugin;
    }
}
