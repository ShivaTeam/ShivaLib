package team.shiva.core.profile.grant.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import team.shiva.core.profile.grant.Grant;
import team.shiva.core.util.BaseEvent;

import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public class GrantAppliedEvent extends BaseEvent {

	private Player player;
	private Grant grant;

}
