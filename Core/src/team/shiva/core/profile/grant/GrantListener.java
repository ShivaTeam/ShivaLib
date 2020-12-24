package team.shiva.core.profile.grant;

import team.shiva.core.Core;
import team.shiva.core.network.packet.PacketDeleteGrant;
import team.shiva.core.profile.Profile;
import team.shiva.core.profile.grant.event.GrantAppliedEvent;
import team.shiva.core.profile.grant.event.GrantExpireEvent;
import team.shiva.core.profile.grant.procedure.GrantProcedure;
import team.shiva.core.profile.grant.procedure.GrantProcedureStage;
import team.shiva.core.profile.grant.procedure.GrantProcedureType;
import team.shiva.core.util.CC;
import team.shiva.core.util.TimeUtil;
import team.shiva.core.util.callback.TypeCallback;
import team.shiva.core.util.menu.menus.ConfirmMenu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GrantListener implements Listener {

	@EventHandler
	public void onGrantAppliedEvent(GrantAppliedEvent event) {
		Player player = event.getPlayer();
		Grant grant = event.getGrant();

		player.sendMessage(CC.GREEN + ("A `{rank}` grant has been applied to you for {time-remaining}.")
				.replace("{rank}", grant.getRank().getDisplayName())
				.replace("{time-remaining}", grant.getDuration() == Integer.MAX_VALUE ?
						"forever" : TimeUtil.millisToRoundedTime((grant.getAddedAt() + grant.getDuration()) -
						                                         System.currentTimeMillis())));

		Profile profile = Profile.getByUuid(player.getUniqueId());
		profile.setupBukkitPlayer(player);
	}

	@EventHandler
	public void onGrantExpireEvent(GrantExpireEvent event) {
		Player player = event.getPlayer();
		Grant grant = event.getGrant();

		player.sendMessage(CC.RED + ("Your `{rank}` grant has expired.")
				.replace("{rank}", grant.getRank().getDisplayName()));

		Profile profile = Profile.getByUuid(player.getUniqueId());
		profile.setupBukkitPlayer(player);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		if (!event.getPlayer().hasPermission("zoot.staff.grant")) {
			return;
		}

		GrantProcedure procedure = GrantProcedure.getByPlayer(event.getPlayer());

		if (procedure != null && procedure.getStage() == GrantProcedureStage.REQUIRE_TEXT) {
			event.setCancelled(true);

			if (event.getMessage().equalsIgnoreCase("cancel")) {
				GrantProcedure.getProcedures().remove(procedure);
				event.getPlayer().sendMessage(CC.RED + "You have cancelled the grant procedure.");
				return;
			}

			if (procedure.getType() == GrantProcedureType.REMOVE) {
				new ConfirmMenu(CC.YELLOW + "Delete this grant?", new TypeCallback<Boolean>() {
					@Override
					public void callback(Boolean data) {
						if (data) {
							procedure.getGrant().setRemovedBy(event.getPlayer().getUniqueId());
							procedure.getGrant().setRemovedAt(System.currentTimeMillis());
							procedure.getGrant().setRemovedReason(event.getMessage());
							procedure.getGrant().setRemoved(true);
							procedure.finish();
							event.getPlayer().sendMessage(CC.GREEN + "The grant has been removed.");

							Core.get().getPidgin().sendPacket(new PacketDeleteGrant(procedure.getRecipient().getUuid(),
									procedure.getGrant()));
						} else {
							procedure.cancel();
							event.getPlayer().sendMessage(CC.RED + "You did not confirm to remove the grant.");
						}
					}
				}, true) {
					@Override
					public void onClose(Player player) {
						if (!isClosedByMenu()) {
							procedure.cancel();
							event.getPlayer().sendMessage(CC.RED + "You did not confirm to remove the grant.");
						}
					}
				}.openMenu(event.getPlayer());
			}
		}
	}

}
