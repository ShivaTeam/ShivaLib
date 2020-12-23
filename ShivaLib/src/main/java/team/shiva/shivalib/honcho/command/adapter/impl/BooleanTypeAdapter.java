package team.shiva.shivalib.honcho.command.adapter.impl;

import team.shiva.shivalib.honcho.command.adapter.CommandTypeAdapter;
import java.util.HashMap;
import java.util.Map;

public class BooleanTypeAdapter implements CommandTypeAdapter {

    private static final Map<String, Boolean> MAP = new HashMap<>();

    static {
        MAP.put("true", true);
        MAP.put("yes", true);

        MAP.put("false", false);
        MAP.put("no", false);
    }

    @Override
    public <T> T convert(String string, Class<T> type) {
        return type.cast(MAP.get(string.toLowerCase()));
    }

}
