package team.shiva.core.util.json;

import com.google.gson.JsonObject;

public interface JsonDeserializer<T> {

	T deserialize(JsonObject object);

}
