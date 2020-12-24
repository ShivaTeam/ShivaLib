package us.zonix.anticheat.event;

import java.util.Map;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ModListRetrieveEvent extends PlayerEvent {

	private final Map<String, String> mods;

	public ModListRetrieveEvent(Player player, Map<String, String> mods) {
		super(player);

		this.mods = mods;
	}

}
