package team.shiva.core.util;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import team.shiva.core.chat.util.ChatComponentBuilder;

public class ChatHelper {

	public static ClickEvent click(String command) {
		return new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
	}

	public static ClickEvent suggest(String command) {
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command);
	}

	public static HoverEvent hover(String text) {
		return new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentBuilder("").parse(text).create());
	}

}
