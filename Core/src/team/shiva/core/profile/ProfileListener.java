package team.shiva.core.profile;

import org.bukkit.ChatColor;
import team.shiva.core.Core;
import team.shiva.core.CoreAPI;
import team.shiva.core.Locale;
import team.shiva.core.cache.RedisPlayerData;
import team.shiva.core.network.packet.PacketStaffChat;
import team.shiva.core.profile.punishment.Punishment;
import team.shiva.core.profile.punishment.PunishmentType;
import team.shiva.core.util.CC;

import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ProfileListener implements Listener {

	private static Core plugin = Core.get();

	@EventHandler
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
		Player player = Bukkit.getPlayer(event.getUniqueId());

		// Need to check if player is still logged in when receiving another login attempt
		// This happens when a player using a custom client can access the server list while in-game (and reconnecting)
		if (player != null && player.isOnline()) {
			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			event.setKickMessage(CC.RED + "You tried to login too quickly after disconnecting.\nTry again in a few seconds.");
			plugin.getServer().getScheduler().runTask(plugin, () -> player.kickPlayer(CC.RED + "Duplicate login kick"));
			return;
		}

		Profile profile = null;

		try {
			profile = new Profile(event.getName(), event.getUniqueId());

			if (!profile.isLoaded()) {
				event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
				event.setKickMessage(Locale.FAILED_TO_LOAD_PROFILE.format());
				return;
			}

			if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
				handleBan(event, profile.getActivePunishmentByType(PunishmentType.BAN));
				return;
			}

			profile.setName(event.getName());

			if (profile.getFirstSeen() == null) {
				profile.setFirstSeen(System.currentTimeMillis());
			}

			profile.setLastSeen(System.currentTimeMillis());

			profile.setCurrentAddress(event.getAddress().getHostAddress());

			if (!profile.getIpAddresses().contains(event.getAddress().getHostAddress())) {
				profile.getIpAddresses().add(event.getAddress().getHostAddress());
			}

			List<Profile> alts = Profile.getByIpAddress(event.getAddress().getHostAddress());

			for (Profile alt : alts) {
				if (alt.getActivePunishmentByType(PunishmentType.BAN) != null) {
					handleBan(event, alt.getActivePunishmentByType(PunishmentType.BAN));
					event.setKickMessage(event.getKickMessage()+"\n"+ ChatColor.YELLOW + "This ban is related to "+ ChatColor.RED + alt.getName());
					return;
				}
			}

			profile.save();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (profile == null || !profile.isLoaded()) {
			event.setKickMessage(Locale.FAILED_TO_LOAD_PROFILE.format());
			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			return;
		}

		Profile.getProfiles().put(profile.getUuid(), profile);

		RedisPlayerData playerData = new RedisPlayerData(event.getUniqueId(), event.getName());
		playerData.setLastAction(RedisPlayerData.LastAction.JOINING_SERVER);
		playerData.setLastSeenServer(Bukkit.getServerId());
		playerData.setLastSeenAt(System.currentTimeMillis());

		plugin.getRedisCache().updatePlayerData(playerData);
		plugin.getRedisCache().updateNameAndUUID(event.getName(), event.getUniqueId());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.setupBukkitPlayer(player);

		if (player.hasPermission("plugin..staff")) {
			player.sendMessage(CC.GOLD + "Your staff mode is currently: " +
					(profile.getStaffOptions().staffModeEnabled() ? CC.GREEN + "Enabled" : CC.RED + "Disabled"));
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Profile profile = Profile.getProfiles().remove(event.getPlayer().getUniqueId());
		profile.setLastSeen(System.currentTimeMillis());

		if (profile.isLoaded()) {
			new BukkitRunnable() {
				@Override
				public void run() {
					profile.save();
				}
			}.runTaskAsynchronously(Core.get());
		}

		RedisPlayerData playerData = new RedisPlayerData(event.getPlayer().getUniqueId(), event.getPlayer().getName());
		playerData.setLastAction(RedisPlayerData.LastAction.LEAVING_SERVER);
		playerData.setLastSeenServer(Bukkit.getServerId());
		playerData.setLastSeenAt(System.currentTimeMillis());

		plugin.getRedisCache().updatePlayerData(playerData);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		Profile profile = Profile.getByUuid(event.getPlayer().getUniqueId());

		if (profile.getStaffOptions().staffChatModeEnabled()) {
			if (profile.getStaffOptions().staffModeEnabled()) {
				Core.get().getPidgin().sendPacket(new PacketStaffChat(
						CoreAPI.getColoredName(event.getPlayer()), Bukkit.getServerId(), event.getMessage()));
			} else {
				event.getPlayer().sendMessage(CC.RED + "You must enable staff mode or disable staff chat mode.");
			}

			event.setCancelled(true);
		}
	}

	private void handleBan(AsyncPlayerPreLoginEvent event, Punishment punishment) {
		event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
		event.setKickMessage(punishment.getKickMessage());
	}

}
