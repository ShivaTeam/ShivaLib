package team.shiva.core.profile.grant.command;

import team.shiva.core.Locale;
import team.shiva.core.profile.Profile;
import team.shiva.core.profile.grant.menu.GrantsListMenu;
import team.shiva.shivalib.honcho.command.CPL;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "grants", async = true, permission = "zoot.grants.show")
public class GrantsCommand {

	public void execute(Player player, @CPL("player") Profile profile) {
		if (profile == null || !profile.isLoaded()) {
			player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		new GrantsListMenu(profile).openMenu(player);
	}

}
