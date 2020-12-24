package team.shiva.core.network.packet;

import com.google.gson.JsonObject;

import java.util.UUID;
import lombok.Getter;
import net.evilblock.pidgin.packet.Packet;
import team.shiva.core.profile.punishment.Punishment;
import team.shiva.core.util.json.JsonChain;

public class PacketBroadcastPunishment implements Packet {

	@Getter private Punishment punishment;
	@Getter private String staff;
	@Getter private String target;
	@Getter private UUID targetUuid;
	@Getter private boolean silent;

	public PacketBroadcastPunishment() {

	}

	public PacketBroadcastPunishment(Punishment punishment, String staff, String target, UUID targetUuid, boolean silent) {
		this.punishment = punishment;
		this.staff = staff;
		this.target = target;
		this.targetUuid = targetUuid;
		this.silent = silent;
	}

	@Override
	public int id() {
		return 2;
	}

	@Override
	public JsonObject serialize() {
		return new JsonChain()
				.add("punishment", Punishment.SERIALIZER.serialize(punishment))
				.addProperty("staff", staff)
				.addProperty("target", target)
				.addProperty("targetUuid", targetUuid.toString())
				.addProperty("silent", silent)
				.get();
	}

	@Override
	public void deserialize(JsonObject object) {
		punishment = Punishment.DESERIALIZER.deserialize(object.get("punishment").getAsJsonObject());
		staff = object.get("staff").getAsString();
		target = object.get("target").getAsString();
		targetUuid = UUID.fromString(object.get("targetUuid").getAsString());
		silent = object.get("silent").getAsBoolean();
	}

}
