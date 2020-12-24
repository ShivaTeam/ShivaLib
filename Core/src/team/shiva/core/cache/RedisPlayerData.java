package team.shiva.core.cache;

import com.google.gson.JsonObject;

import java.util.UUID;
import lombok.Data;
import team.shiva.core.util.TimeUtil;
import team.shiva.core.util.json.JsonChain;

@Data
public class RedisPlayerData {

	private UUID uuid;
	private String username;
	private LastAction lastAction;
	private String lastSeenServer;
	private long lastSeenAt;

	public RedisPlayerData(JsonObject object) {
		this.uuid = UUID.fromString(object.get("uuid").getAsString());
		this.username = object.get("name").getAsString();
		this.lastAction = LastAction.valueOf(object.get("lastAction").getAsString());
		this.lastSeenServer = object.get("lastSeenServer").getAsString();
		this.lastSeenAt = object.get("lastSeenAt").getAsLong();
	}

	public RedisPlayerData(UUID uuid, String username) {
		this.uuid = uuid;
		this.username = username;
	}

	public JsonObject getJson() {
		return new JsonChain()
				.addProperty("uuid", uuid.toString())
				.addProperty("name", username)
				.addProperty("lastAction", lastAction.name())
				.addProperty("lastSeenServer", lastSeenServer)
				.addProperty("lastSeenAt", lastSeenAt)
				.get();
	}

	public String getTimeAgo() {
		return TimeUtil.millisToRoundedTime(System.currentTimeMillis() - lastSeenAt) + " ago";
	}

	public enum LastAction {
		LEAVING_SERVER,
		JOINING_SERVER
	}

}
