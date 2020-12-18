package team.shiva.shivalib.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class UUIDUtils {
    public static String name(final UUID uuid) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return player == null ? "null" : player.getName();
    }

    public static UUID uuid(final String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return player == null ? null : player.getUniqueId();
    }
}
