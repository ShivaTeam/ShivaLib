package team.shiva.core.profile.staff.command;

import team.shiva.core.Locale;
import team.shiva.core.profile.Profile;
import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CPL;
import team.shiva.shivalib.honcho.command.CommandMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "alts", async = true, permission = "zoot.staff.alts")
public class AltsCommand {

	public void execute(CommandSender sender, @CPL("player") Profile profile) {
		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		List<Profile> alts = new ArrayList<>();

		for (UUID altUuid : profile.getKnownAlts()) {
			Profile altProfile = Profile.getByUuid(altUuid);

			if (altProfile != null && altProfile.isLoaded()) {
				alts.add(altProfile);
			}
		}

		if (alts.isEmpty()) {
			sender.sendMessage(CC.RED + "This player has no known alt accounts.");
		} else {
			StringBuilder builder = new StringBuilder();

			for (Profile altProfile : alts) {
				builder.append(altProfile.getName());
				builder.append(", ");
			}

			sender.sendMessage(CC.GOLD + "Alts: " + CC.RESET + builder.toString());
		}
	}

}
