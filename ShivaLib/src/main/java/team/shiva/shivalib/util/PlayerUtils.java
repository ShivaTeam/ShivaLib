package team.shiva.shivalib.util;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.potion.PotionEffect;
import team.shiva.shivalib.ShivaLib;
import team.shiva.shivalib.protocol.InventoryAdapter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerUtils {

    private static Field STATUS_PACKET_ID_FIELD;
    private static Field STATUS_PACKET_STATUS_FIELD;
    private static Field SPAWN_PACKET_ID_FIELD;
    static {
        try {
            STATUS_PACKET_ID_FIELD = PacketPlayOutEntityStatus.class.getDeclaredField("a");
            STATUS_PACKET_ID_FIELD.setAccessible(true);
            STATUS_PACKET_STATUS_FIELD = PacketPlayOutEntityStatus.class.getDeclaredField("b");
            STATUS_PACKET_STATUS_FIELD.setAccessible(true);
            SPAWN_PACKET_ID_FIELD = PacketPlayOutNamedEntitySpawn.class.getDeclaredField("a");
            SPAWN_PACKET_ID_FIELD.setAccessible(true);
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    public static void animateDeath(Player player) {
        int entityId = EntityUtils.getFakeEntityId();
        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(((CraftPlayer)player).getHandle());
        PacketPlayOutEntityStatus statusPacket = new PacketPlayOutEntityStatus();
        try {
            SPAWN_PACKET_ID_FIELD.set(spawnPacket, entityId);
            STATUS_PACKET_ID_FIELD.set(statusPacket, entityId);
            STATUS_PACKET_STATUS_FIELD.set(statusPacket, (byte)3);
            int radius = MinecraftServer.getServer().getPlayerList().d();
            HashSet<Player> sentTo = new HashSet<>();
            for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
                Player watcher;
                if (!(entity instanceof Player) || (watcher = (Player)entity).getUniqueId().equals(player.getUniqueId())) continue;
                ((CraftPlayer)watcher).getHandle().playerConnection.sendPacket(spawnPacket);
                ((CraftPlayer)watcher).getHandle().playerConnection.sendPacket(statusPacket);
                sentTo.add(watcher);
            }
            Bukkit.getScheduler().runTaskLater(ShivaLib.getInstance(), () -> {
                for (Player watcher : sentTo) {
                    ((CraftPlayer)watcher).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entityId));
                }
            }, 40L);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void animateDeath(Player player, Player watcher) {
        int entityId = EntityUtils.getFakeEntityId();
        PacketPlayOutNamedEntitySpawn spawnPacket = new PacketPlayOutNamedEntitySpawn(((CraftPlayer)player).getHandle());
        PacketPlayOutEntityStatus statusPacket = new PacketPlayOutEntityStatus();
        try {
            SPAWN_PACKET_ID_FIELD.set(spawnPacket, entityId);
            STATUS_PACKET_ID_FIELD.set(statusPacket, entityId);
            STATUS_PACKET_STATUS_FIELD.set(statusPacket, (byte)3);
            ((CraftPlayer)watcher).getHandle().playerConnection.sendPacket(spawnPacket);
            ((CraftPlayer)watcher).getHandle().playerConnection.sendPacket(statusPacket);
            Bukkit.getScheduler().runTaskLater(ShivaLib.getInstance(), () -> ((CraftPlayer)watcher).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entityId)), 40L);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetInventory(Player player) {
        resetInventory(player, null);
    }

    public static void resetInventory(Player player, GameMode gameMode) {
        resetInventory(player, gameMode, false);
    }

    public static void resetInventory(Player player, GameMode gameMode, boolean skipInvReset) {
        player.setHealth(player.getMaxHealth());
        player.setFallDistance(0F);
        player.setFoodLevel(20);
        player.setSaturation(10F);
        player.setLevel(0);
        player.setExp(0F);

        if (!skipInvReset) {
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }

        player.setFireTicks(0);

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }

        if (gameMode != null && player.getGameMode() != gameMode) {
            player.setGameMode(gameMode);
        }

        player.setAllowFlight(false);
        player.setFlying(false);
    }

    public static List<String> mapToNames(Collection<UUID> uuids) {
        return uuids.stream().map(UUIDUtils::name).collect(Collectors.toList());
    }

    public static String getFormattedName(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            return player.getDisplayName();
        } else {
            return UUIDUtils.name(uuid);
        }
    }

    public static Player getDamageSource(Entity damager) {
        Projectile projectile;
        Player playerDamager = null;
        if (damager instanceof Player) {
            playerDamager = (Player)damager;
        } else if (damager instanceof Projectile && (projectile = (Projectile)damager).getShooter() instanceof Player) {
            playerDamager = (Player)projectile.getShooter();
        }
        return playerDamager;
    }

    public static boolean hasOpenInventory(Player player) {
        return PlayerUtils.hasOwnInventoryOpen(player) || PlayerUtils.hasOtherInventoryOpen(player);
    }

    public static boolean hasOwnInventoryOpen(Player player) {
        return InventoryAdapter.getCurrentlyOpen().contains(player.getUniqueId());
    }

    public static boolean hasOtherInventoryOpen(Player player) {
        return ((CraftPlayer)player).getHandle().activeContainer.windowId != 0;
    }

    public static int getPing(Player player) {
        return ((CraftPlayer)player).getHandle().ping;
    }
}
