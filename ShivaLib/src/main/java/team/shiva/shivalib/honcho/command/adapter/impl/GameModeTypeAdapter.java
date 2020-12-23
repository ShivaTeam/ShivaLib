package team.shiva.shivalib.honcho.command.adapter.impl;

import team.shiva.shivalib.honcho.command.adapter.CommandTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.GameMode;

public class GameModeTypeAdapter implements CommandTypeAdapter {

    private static final Map<String, GameMode> MAP = new HashMap<>();

    static {
        MAP.put("0", GameMode.SURVIVAL);
        MAP.put("s", GameMode.SURVIVAL);
        MAP.put("survival", GameMode.SURVIVAL);

        MAP.put("1", GameMode.CREATIVE);
        MAP.put("c", GameMode.CREATIVE);
        MAP.put("creative", GameMode.CREATIVE);
    }

    @Override
    public <T> T convert(String string, Class<T> type) {
        return type.cast(MAP.get(string.toLowerCase()));
    }

    @Override
    public <T> List<String> tabComplete(String string, Class<T> type) {
        if (string.isEmpty()) {
            return new ArrayList<>(MAP.keySet());
        } else {
            List<String> completed = new ArrayList<>();

            for (String key : MAP.keySet()) {
                if (key.toLowerCase().startsWith(string)) {
                    completed.add(key);
                }
            }

            return completed;
        }
    }

}
