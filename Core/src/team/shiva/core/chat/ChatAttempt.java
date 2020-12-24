package team.shiva.core.chat;

import lombok.Getter;
import lombok.Setter;
import team.shiva.core.chat.filter.ChatFilter;
import team.shiva.core.profile.punishment.Punishment;

@Getter
@Setter
public class ChatAttempt {

	private Response response;
	private ChatFilter filterFlagged;
	private Punishment punishment;
	private Object value;

	public ChatAttempt(Response response) {
		this.response = response;
	}

	public ChatAttempt(Response response, ChatFilter filterFlagged) {
		this.response = response;
		this.filterFlagged = filterFlagged;
	}

	public ChatAttempt(Response response, Punishment punishment) {
		this.response = response;
		this.punishment = punishment;
	}

	public enum Response {
		ALLOWED,
		MESSAGE_FILTERED,
		PLAYER_MUTED,
		CHAT_MUTED,
		CHAT_DELAYED
	}

}
