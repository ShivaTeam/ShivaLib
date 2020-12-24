package team.shiva.core.rank;

import team.shiva.shivalib.honcho.command.adapter.CommandTypeAdapter;
import java.util.ArrayList;
import java.util.List;

public class RankTypeAdapter implements CommandTypeAdapter {

	@Override
	public <T> T convert(String string, Class<T> type) {
		return type.cast(Rank.getRankByDisplayName(string));
	}

	@Override
	public <T> List<String> tabComplete(String string, Class<T> type) {
		List<String> completed = new ArrayList<>();

		for (Rank rank : Rank.getRanks().values()) {
			if (rank.getDisplayName().toLowerCase().startsWith(string.toLowerCase())) {
				completed.add(rank.getDisplayName());
			}
		}

		return completed;
	}

}
