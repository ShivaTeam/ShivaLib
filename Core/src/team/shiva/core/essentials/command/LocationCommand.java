package team.shiva.core.essentials.command;

import team.shiva.core.util.LocationUtil;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "loc", permission = "zoot.loc")
public class LocationCommand {

	public void execute(Player player) {
		player.sendMessage(LocationUtil.serialize(player.getLocation()));
		System.out.println(LocationUtil.serialize(player.getLocation()));
	}

}
