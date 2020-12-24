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
import java.util.UUID;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "warn", permission = "zoot.staff.warn", async = true, options = "s")
public class WarnCommand {

	public void execute(CommandSender sender, CommandOption option, @CPL("player") Profile profile, String reason) {
		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
				.getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

		Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.WARN, System.currentTimeMillis(),
				reason, -1);

		if (sender instanceof Player) {
			punishment.setAddedBy(((Player) sender).getUniqueId());
		}

		profile.getPunishments().add(punishment);
		profile.save();

		Player player = profile.getPlayer();

		if (player != null) {
			String senderName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender).getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";
			player.sendMessage(CC.RED + "You have been warned by " + senderName + CC.RED + ".");
			player.sendMessage(CC.RED + "The reason for this punishment: " + CC.WHITE + punishment.getAddedReason());
		}

		Core.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
				profile.getColoredUsername(), profile.getUuid(), option != null));
	}

}
