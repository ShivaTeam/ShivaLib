package team.shiva.core.network.packet;

import com.google.gson.JsonObject;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.evilblock.pidgin.packet.Packet;
import team.shiva.core.util.json.JsonChain;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketClearPunishments implements Packet {

	private UUID uuid;

	@Override
	public int id() {
		return 14;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.addProperty("uuid", uuid.toString())
				.get();
	}

	@Override
	public void deserialize(JsonObject object) {
		uuid = UUID.fromString(object.get("uuid").getAsString());
	}

}
