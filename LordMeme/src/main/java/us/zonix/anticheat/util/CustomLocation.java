package us.zonix.anticheat.util;

import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;


@Getter
@Setter
@AllArgsConstructor
public class CustomLocation {

	private final long timestamp = System.currentTimeMillis();

	private String world;

	private double x;
	private double y;
	private double z;

	private float yaw;
	private float pitch;

	public CustomLocation(double x, double y, double z) {
		this(x, y, z, 0.0F, 0.0F);
	}

	public CustomLocation(String world, double x, double y, double z) {
		this(world, x, y, z, 0.0F, 0.0F);
	}

	public CustomLocation(double x, double y, double z, float yaw, float pitch) {
		this("world", x, y, z, yaw, pitch);
	}

	public static CustomLocation fromBukkitLocation(Location location) {
		return new CustomLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(),
				location.getYaw(), location.getPitch());
	}

	public static CustomLocation stringToLocation(String string) {
		String[] split = string.split(", ");

		double x = Double.parseDouble(split[0]);
		double y = Double.parseDouble(split[1]);
		double z = Double.parseDouble(split[2]);

		CustomLocation customLocation = new CustomLocation(x, y, z);

		if (split.length == 4) {
			customLocation.setWorld(split[3]);
		} else if (split.length >= 5) {
			customLocation.setYaw(Float.parseFloat(split[3]));
			customLocation.setPitch(Float.parseFloat(split[4]));

			if (split.length >= 6) {
				customLocation.setWorld(split[5]);
			}
		}
		return customLocation;
	}

	public static String locationToString(CustomLocation loc) {
		StringJoiner joiner = new StringJoiner(", ");
		joiner.add(Double.toString(loc.getX()));
		joiner.add(Double.toString(loc.getY()));
		joiner.add(Double.toString(loc.getZ()));
		if (loc.getYaw() == 0.0f && loc.getPitch() == 0.0f) {
			if (loc.getWorld().equals("world")) {
				return joiner.toString();
			} else {
				joiner.add(loc.getWorld());
				return joiner.toString();
			}
		} else {
			joiner.add(Float.toString(loc.getYaw()));
			joiner.add(Float.toString(loc.getPitch()));
			if (loc.getWorld().equals("world")) {
				return joiner.toString();
			} else {
				joiner.add(loc.getWorld());
				return joiner.toString();
			}
		}
	}

	public Location toBukkitLocation() {
		return new Location(this.toBukkitWorld(), this.x, this.y, this.z, this.yaw, this.pitch);
	}

	public double getGroundDistanceTo(CustomLocation location) {
		return Math.sqrt(Math.pow(this.x - location.x, 2) + Math.pow(this.z - location.z, 2));
	}

	public double getDistanceTo(CustomLocation location) {
		return Math.sqrt(Math.pow(this.x - location.x, 2) + Math.pow(this.y - location.y, 2) + Math.pow(this.z - location.z, 2));
	}

	public World toBukkitWorld() {
		if (this.world == null) {
			return Bukkit.getServer().getWorlds().get(0);
		} else {
			return Bukkit.getServer().getWorld(this.world);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CustomLocation)) {
			return false;
		}

		CustomLocation location = (CustomLocation) obj;
		return location.x == this.x && location.y == this.y && location.z == this.z
				&& location.pitch == this.pitch && location.yaw == this.yaw;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("x", this.x)
				.append("y", this.y)
				.append("z", this.z)
				.append("yaw", this.yaw)
				.append("pitch", this.pitch)
				.append("world", this.world)
				.append("timestamp", this.timestamp)
				.toString();
	}

}
