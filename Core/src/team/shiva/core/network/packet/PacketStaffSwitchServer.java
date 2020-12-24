package team.shiva.core.network.packet;

import com.google.gson.JsonObject;

import lombok.Getter;
import net.evilblock.pidgin.packet.Packet;
import team.shiva.core.util.json.JsonChain;

@Getter
public class PacketStaffSwitchServer implements Packet {

	private String playerName;
	private String fromServerName;
	private String toServerName;

	public PacketStaffSwitchServer() {

	}

	public PacketStaffSwitchServer(String playerName, String fromServerName, String toServerName) {
		this.playerName = playerName;
		this.fromServerName = fromServerName;
		this.toServerName = toServerName;
	}

	@Override
	public int id() {
		return 9;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.addProperty("playerName", playerName)
				.addProperty("fromServerName", fromServerName)
				.addProperty("toServerName", toServerName)
				.get();
	}

	@Override
	public void deserialize(JsonObject jsonObject) {
		playerName = jsonObject.get("playerName").getAsString();
		fromServerName = jsonObject.get("fromServerName").getAsString();
		toServerName = jsonObject.get("toServerName").getAsString();
	}

}
