package team.shiva.shivalib.honcho.command.adapter;

import java.util.ArrayList;
import java.util.List;

public interface CommandTypeAdapter {

    <T> T convert(String string, Class<T> type);

    default <T> List<String> tabComplete(String string, Class<T> type) {
        return new ArrayList<>();
    }

}
