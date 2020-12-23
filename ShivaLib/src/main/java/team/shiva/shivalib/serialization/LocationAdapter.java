package team.shiva.shivalib.serialization;

import com.google.gson.*;
import org.bukkit.Location;
import org.bukkit.World;
import team.shiva.shivalib.ShivaLib;

import java.lang.reflect.Type;

public class LocationAdapter
implements JsonDeserializer<Location>,
JsonSerializer<Location> {
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        return LocationAdapter.toJson(src);
    }

    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocationAdapter.fromJson(json);
    }

    public static JsonObject toJson(Location location) {
        if (location == null) {
            return null;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("world", location.getWorld().getName());
        jsonObject.addProperty("x", (Number)location.getX());
        jsonObject.addProperty("y", (Number)location.getY());
        jsonObject.addProperty("z", (Number)location.getZ());
        jsonObject.addProperty("yaw", (Number)Float.valueOf(location.getYaw()));
        jsonObject.addProperty("pitch", (Number)Float.valueOf(location.getPitch()));
        return jsonObject;
    }

    public static Location fromJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return null;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        World world = ShivaLib.getInstance().getServer().getWorld(jsonObject.get("world").getAsString());
        double x = jsonObject.get("x").getAsDouble();
        double y = jsonObject.get("y").getAsDouble();
        double z = jsonObject.get("z").getAsDouble();
        float yaw = jsonObject.get("yaw").getAsFloat();
        float pitch = jsonObject.get("pitch").getAsFloat();
        return new Location(world, x, y, z, yaw, pitch);
    }
}

