package team.shiva.shivalib.serialization;

import java.lang.reflect.Type;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectAdapter
implements JsonDeserializer<PotionEffect>,
JsonSerializer<PotionEffect> {
    public JsonElement serialize(PotionEffect src, Type typeOfSrc, JsonSerializationContext context) {
        return PotionEffectAdapter.toJson(src);
    }

    public PotionEffect deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return PotionEffectAdapter.fromJson(json);
    }

    public static JsonObject toJson(PotionEffect potionEffect) {
        if (potionEffect == null) {
            return null;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", potionEffect.getType().getId());
        jsonObject.addProperty("duration", potionEffect.getDuration());
        jsonObject.addProperty("amplifier", potionEffect.getAmplifier());
        jsonObject.addProperty("ambient", potionEffect.isAmbient());
        return jsonObject;
    }

    public static PotionEffect fromJson(JsonElement jsonElement) {
        if (jsonElement == null || !jsonElement.isJsonObject()) {
            return null;
        }
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        PotionEffectType effectType = PotionEffectType.getById((int)jsonObject.get("id").getAsInt());
        int duration = jsonObject.get("duration").getAsInt();
        int amplifier = jsonObject.get("amplifier").getAsInt();
        boolean ambient = jsonObject.get("ambient").getAsBoolean();
        return new PotionEffect(effectType, duration, amplifier, ambient);
    }
}

