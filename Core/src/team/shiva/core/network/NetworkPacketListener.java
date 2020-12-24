package team.shiva.core.network;

import net.evilblock.pidgin.packet.handler.IncomingPacketHandler;
import net.evilblock.pidgin.packet.listener.PacketListener;
import team.shiva.core.Core;
import team.shiva.core.Locale;
import team.shiva.core.network.event.ReceiveStaffChatEvent;
import team.shiva.core.network.packet.PacketAddGrant;
import team.shiva.core.network.packet.PacketBroadcastPunishment;
import team.shiva.core.network.packet.PacketClearPunishments;
import team.shiva.core.network.packet.PacketDeleteGrant;
import team.shiva.core.network.packet.PacketDeleteRank;
import team.shiva.core.network.packet.PacketRefreshRank;
import team.shiva.core.network.packet.PacketStaffChat;
import team.shiva.core.network.packet.PacketStaffJoinNetwork;
import team.shiva.core.network.packet.PacketStaffLeaveNetwork;
import team.shiva.core.network.packet.PacketStaffReport;
import team.shiva.core.network.packet.PacketStaffRequest;
import team.shiva.core.network.packet.PacketStaffSwitchServer;
import team.shiva.core.profile.Profile;
import team.shiva.core.profile.grant.Grant;
import team.shiva.core.profile.grant.event.GrantAppliedEvent;
import team.shiva.core.profile.grant.event.GrantExpireEvent;
import team.shiva.core.profile.punishment.Punishment;
import team.shiva.core.rank.Rank;

import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class NetworkPacketListener implements PacketListener {

	private Core core;

	public NetworkPacketListener(Core core) {
		this.core = core;
	}

	@IncomingPacketHandler
	public void onAddGrant(PacketAddGrant packet) {
		Player player = Bukkit.getPlayer(packet.getPlayerUuid());
		Grant grant = packet.getGrant();

		if (player != null) {
			Profile profile = Profile.getProfiles().get(player.getUniqueId());
			profile.getGrants().removeIf(other -> Objects.equals(other, grant));
			profile.getGrants().add(grant);

			new GrantAppliedEvent(player, grant);
		}
	}

	@IncomingPacketHandler
	public void onDeleteGrant(PacketDeleteGrant packet) {
		Player player = Bukkit.getPlayer(packet.getPlayerUuid());
		Grant grant = packet.getGrant();

		if (player != null) {
			Profile profile = Profile.getProfiles().get(player.getUniqueId());
			profile.getGrants().removeIf(other -> Objects.equals(other, grant));
			profile.getGrants().add(grant);

			new GrantExpireEvent(player, grant);
		}
	}

	@IncomingPacketHandler
	public void onBroadcastPunishment(PacketBroadcastPunishment packet) {
		Punishment punishment = packet.getPunishment();
		punishment.broadcast(packet.getStaff(), packet.getTarget(), packet.isSilent());

		Player player = Bukkit.getPlayer(packet.getTargetUuid());

		if (player != null) {
			Profile profile = Profile.getProfiles().get(player.getUniqueId());
			profile.getPunishments().removeIf(other -> Objects.equals(other, punishment));
			profile.getPunishments().add(punishment);

			if (punishment.getType().isBan() && !punishment.isRemoved() && !punishment.hasExpired()) {
				new BukkitRunnable() {
					@Override
					public void run() {
						player.kickPlayer(punishment.getKickMessage());
					}
				}.runTask(Core.get());
			}
		}
	}

	@IncomingPacketHandler
	public void onRankRefresh(PacketRefreshRank packet) {
		Rank rank = Rank.getRankByUuid(packet.getUuid());

		if (rank == null) {
			rank = new Rank(packet.getUuid(), packet.getName());
		}

		rank.load();

		Core.broadcastOps("&8[&eNetwork&8] &fRefreshed rank " + rank.getDisplayName());
	}

	@IncomingPacketHandler
	public void onRankDelete(PacketDeleteRank packet) {
		Rank rank = Rank.getRanks().remove(packet.getUuid());

		if (rank != null) {
			Core.broadcastOps("&8[&eNetwork&8] &fDeleted rank " + rank.getDisplayName() );
		}
	}

	@IncomingPacketHandler
	public void onStaffChat(PacketStaffChat packet) {
		core.getServer().getOnlinePlayers().stream()
		    .filter(onlinePlayer -> onlinePlayer.hasPermission("core.staff"))
		    .forEach(onlinePlayer -> {
			    ReceiveStaffChatEvent event = new ReceiveStaffChatEvent(onlinePlayer);

			    core.getServer().getPluginManager().callEvent(event);

			    if (!event.isCancelled()) {
			    	Profile profile = Profile.getProfiles().get(event.getPlayer().getUniqueId());

			    	if (profile != null && profile.getStaffOptions().staffModeEnabled()) {
					    onlinePlayer.sendMessage(Locale.STAFF_CHAT.format(packet.getPlayerName(), packet.getServerName(),
							    packet.getChatMessage()));
				    }
			    }
		    });
	}

	@IncomingPacketHandler
	public void onStaffJoinNetwork(PacketStaffJoinNetwork packet) {
		core.getServer().broadcast(Locale.STAFF_JOIN_NETWORK.format(packet.getPlayerName(), packet.getServerName()),
				"core.staff");
	}

	@IncomingPacketHandler
	public void onStaffLeaveNetwork(PacketStaffLeaveNetwork packet) {
		core.getServer().broadcast(Locale.STAFF_LEAVE_NETWORK.format(packet.getPlayerName()), "core.staff");
	}

	@IncomingPacketHandler
	public void onStaffSwitchServer(PacketStaffSwitchServer packet) {
		core.getServer().broadcast(Locale.STAFF_SWITCH_SERVER.format(packet.getPlayerName(), packet.getToServerName(),
				packet.getFromServerName()), "core.staff");
	}

	@IncomingPacketHandler
	public void onStaffReport(PacketStaffReport packet) {
		List<String> messages = Locale.STAFF_REPORT_BROADCAST.formatLines(packet.getSentBy(), packet.getAccused(),
				packet.getReason(), packet.getServerId(), packet.getServerName());

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("core.staff")) {
				Profile profile = Profile.getByUuid(player.getUniqueId());

				if (profile.getStaffOptions().staffModeEnabled()) {
					for (String message : messages) {
						player.sendMessage(message);
					}
				}
			}
		}
	}

	@IncomingPacketHandler
	public void onStaffRequest(PacketStaffRequest packet) {
		List<String> messages = Locale.STAFF_REQUEST_BROADCAST.formatLines(packet.getSentBy(), packet.getReason(),
				packet.getServerId(), packet.getServerName());

		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission("core.staff")) {
				Profile profile = Profile.getByUuid(player.getUniqueId());

				if (profile.getStaffOptions().staffModeEnabled()) {
					for (String message : messages) {
						player.sendMessage(message);
					}
				}
			}
		}
	}

	@IncomingPacketHandler
	public void onClearGrants(PacketClearPunishments packet) {
		Player player = Bukkit.getPlayer(packet.getUuid());

		if (player != null) {
			Profile profile = Profile.getByUuid(player.getUniqueId());
			profile.getGrants().clear();
		}
	}

	@IncomingPacketHandler
	public void onClearPunishments(PacketClearPunishments packet) {
		Player player = Bukkit.getPlayer(packet.getUuid());

		if (player != null) {
			Profile profile = Profile.getByUuid(player.getUniqueId());
			profile.getPunishments().clear();
		}
	}

}
