package team.shiva.core.util.adapter;

import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.adapter.CommandTypeAdapter;
import java.util.ArrayList;
import java.util.List;

public class ChatColorTypeAdapter implements CommandTypeAdapter {

	@Override
	public <T> T convert(String string, Class<T> type) {
		return type.cast(CC.getColorFromName(string));
	}

	@Override
	public <T> List<String> tabComplete(String string, Class<T> type) {
		final String compare = string.trim().toLowerCase();

		List<String> completed = new ArrayList<>();

		for (String colorName : CC.getColorNames()) {
			if (colorName.startsWith(compare)) {
				completed.add(colorName);
			}
		}

		return completed;
	}

}
