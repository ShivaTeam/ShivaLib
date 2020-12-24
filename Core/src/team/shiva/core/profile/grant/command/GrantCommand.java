package team.shiva.core.profile.grant.command;

import team.shiva.core.Core;
import team.shiva.core.Locale;
import team.shiva.core.network.packet.PacketAddGrant;
import team.shiva.core.profile.Profile;
import team.shiva.core.profile.grant.Grant;
import team.shiva.core.profile.grant.event.GrantAppliedEvent;
import team.shiva.core.rank.Rank;
import team.shiva.core.util.CC;
import team.shiva.core.util.TimeUtil;
import team.shiva.core.util.duration.Duration;
import team.shiva.shivalib.honcho.command.CPL;
import team.shiva.shivalib.honcho.command.CommandMeta;
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "grant", async = true, permission = "zoot.grants.add")
public class GrantCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile, Rank rank, Duration duration, String reason) {
		if (rank == null) {
			sender.sendMessage(Locale.RANK_NOT_FOUND.format());
			return;
		}

		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		if (duration.getValue() == -1) {
			sender.sendMessage(CC.RED + "That duration is not valid.");
			sender.sendMessage(CC.RED + "Example: [perm/1y1m1w1d]");
			return;
		}

		UUID addedBy = sender instanceof Player ? ((Player) sender).getUniqueId() : null;
		Grant grant = new Grant(UUID.randomUUID(), rank, addedBy, System.currentTimeMillis(), reason,
				duration.getValue());

		profile.getGrants().add(grant);
		profile.save();
		profile.activateNextGrant();

		Core.get().getPidgin().sendPacket(new PacketAddGrant(profile.getUuid(), grant));

		sender.sendMessage(CC.GREEN + "You applied a `{rank}` grant to `{player}` for {time-remaining}."
				.replace("{rank}", rank.getDisplayName())
				.replace("{player}", profile.getName())
				.replace("{time-remaining}", duration.getValue() == Integer.MAX_VALUE ? "forever"
						: TimeUtil.millisToRoundedTime(duration.getValue())));

		Player player = profile.getPlayer();

		if (player != null) {
			new GrantAppliedEvent(player, grant).call();
		}
	}

}
