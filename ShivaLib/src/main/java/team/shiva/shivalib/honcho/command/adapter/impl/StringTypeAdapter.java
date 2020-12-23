package team.shiva.shivalib.honcho.command.adapter.impl;

import team.shiva.shivalib.honcho.command.adapter.CommandTypeAdapter;
import org.bukkit.Bukkit;

public class StringTypeAdapter implements CommandTypeAdapter {
    @Override
    public <T> T convert(String string, Class<T> type) {
        return type.cast(string);
    }
}
