package team.shiva.shivalib.honcho.command.adapter.impl;

import team.shiva.shivalib.honcho.command.CommandOption;
import team.shiva.shivalib.honcho.command.adapter.CommandTypeAdapter;

public class CommandOptionTypeAdapter implements CommandTypeAdapter {

    @Override
    public <T> T convert(String string, Class<T> type) {
        if (string.startsWith("-")) {
            return type.cast(new CommandOption(string.substring(1)));
        }

        return null;
    }

}
