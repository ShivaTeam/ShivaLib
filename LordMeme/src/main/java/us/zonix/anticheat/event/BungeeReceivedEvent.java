package us.zonix.anticheat.event;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class BungeeReceivedEvent extends PlayerEvent {

	private final String channel;
	private final String message;

	private final byte[] messageBytes;
	private final boolean isValid;

	public BungeeReceivedEvent(Player player, String channel, String message, byte[] messageBytes, boolean isValid) {
		super(player);
		this.channel = channel;
		this.message = message;
		this.messageBytes = messageBytes;
		this.isValid = isValid;
	}

}
