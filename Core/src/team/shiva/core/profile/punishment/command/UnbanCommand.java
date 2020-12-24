package team.shiva.core.profile.punishment.command;

import team.shiva.core.Core;
import team.shiva.core.Locale;
import team.shiva.core.network.packet.PacketBroadcastPunishment;
import team.shiva.core.profile.Profile;
import team.shiva.core.profile.punishment.Punishment;
import team.shiva.core.profile.punishment.PunishmentType;
import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CPL;
import team.shiva.shivalib.honcho.command.CommandMeta;
import team.shiva.shivalib.honcho.command.CommandOption;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "unban", permission = "zoot.staff.unban", async = true, options = "s")
public class UnbanCommand {

	public void execute(CommandSender sender, CommandOption option, @CPL("player") Profile profile, String reason) {
		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		if (profile.getActivePunishmentByType(PunishmentType.BAN) == null) {
			sender.sendMessage(CC.RED + "That player is not banned.");
			return;
		}

		String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
				.getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

		Punishment punishment = profile.getActivePunishmentByType(PunishmentType.BAN);
		punishment.setRemovedAt(System.currentTimeMillis());
		punishment.setRemovedReason(reason);
		punishment.setRemoved(true);

		if (sender instanceof Player) {
			punishment.setRemovedBy(((Player) sender).getUniqueId());
		}

		profile.save();

		Core.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
				profile.getColoredUsername(), profile.getUuid(), option != null));
	}

}
