package team.shiva.core.profile.punishment.command;

import team.shiva.core.Core;
import team.shiva.core.Locale;
import team.shiva.core.cache.RedisPlayerData;
import team.shiva.core.profile.Profile;
import team.shiva.core.profile.punishment.menu.PunishmentsListMenu;
import team.shiva.shivalib.honcho.command.CPL;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = { "check", "c" }, permission = "zoot.staff.check", async = true)
public class CheckCommand {

	public void execute(Player player, @CPL("player") Profile profile) {
		if (profile == null || !profile.isLoaded()) {
			player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		RedisPlayerData redisPlayerData = Core.get().getRedisCache().getPlayerData(profile.getUuid());

		if (redisPlayerData == null) {
			player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		new PunishmentsListMenu(profile, redisPlayerData).openMenu(player);
	}

}
