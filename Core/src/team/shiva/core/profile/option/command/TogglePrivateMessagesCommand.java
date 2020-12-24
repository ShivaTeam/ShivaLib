package team.shiva.core.profile.option.command;

import team.shiva.core.Locale;
import team.shiva.core.profile.Profile;
import team.shiva.shivalib.honcho.command.CommandMeta;

import org.bukkit.entity.Player;

@CommandMeta(label = { "togglepm", "togglepms", "tpm", "tpms" })
public class TogglePrivateMessagesCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getOptions().receivingNewConversations(!profile.getOptions().receivingNewConversations());
        profile.getConversations().expireAllConversations();

        if (profile.getOptions().receivingNewConversations()) {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGES_ENABLED.format());
        } else {
            player.sendMessage(Locale.OPTIONS_PRIVATE_MESSAGES_DISABLED.format());
        }
    }

}
