package team.shiva.core.network.packet;

import com.google.gson.JsonObject;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.evilblock.pidgin.packet.Packet;
import team.shiva.core.util.json.JsonChain;

@AllArgsConstructor
@Getter
public class PacketDeleteRank implements Packet {

	private UUID uuid;

	public PacketDeleteRank() {

	}

	@Override
	public int id() {
		return 4;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.addProperty("uuid", uuid.toString())
				.get();
	}

	@Override
	public void deserialize(JsonObject jsonObject) {
		uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
	}

}