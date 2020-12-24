package team.shiva.core.profile.conversation;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import team.shiva.core.profile.Profile;

import org.bukkit.entity.Player;

public class ProfileConversations {

	@Getter private final Profile profile;
	@Getter private Map<UUID, Conversation> conversations;

	public ProfileConversations(Profile profile) {
		this.profile = profile;
		this.conversations = new HashMap<>();
	}

	public boolean canBeMessagedBy(Player player) {
		if (!profile.getOptions().receivingNewConversations()) {
			return conversations.containsKey(player.getUniqueId());
		}

		return true;
	}

	public Conversation getOrCreateConversation(Player target) {
		Player sender = profile.getPlayer();

		if (sender != null) {
			Conversation conversation = conversations.get(target.getUniqueId());

			if (conversation == null) {
				conversation = new Conversation(profile.getUuid(), target.getUniqueId());
			}

			return conversation;
		}

		return null;
	}

	public Conversation getLastRepliedConversation() {
		List<Conversation> list = conversations
				.values()
				.stream()
				.sorted(Comparator.comparingLong(Conversation::getLastMessageSentAt))
				.collect(Collectors.toList());

		Collections.reverse(list);

		return list.isEmpty() ? null : list.get(0);
	}

	public void expireAllConversations() {
		this.conversations.clear();
	}

}
