package team.shiva.core.profile.conversation.command;

import team.shiva.core.profile.Profile;
import team.shiva.core.profile.conversation.Conversation;
import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CommandMeta;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandMeta(label = { "reply", "r" })
public class ReplyCommand {

    public void execute(Player player, String message) {
        Profile playerProfile = Profile.getByUuid(player.getUniqueId());
        Conversation conversation = playerProfile.getConversations().getLastRepliedConversation();

        if (conversation != null) {
            if (conversation.validate()) {
                conversation.sendMessage(player, Bukkit.getPlayer(conversation.getPartner(player.getUniqueId())), message);
            } else {
                player.sendMessage(CC.RED + "You can no longer reply to that player.");
            }
        } else {
            player.sendMessage(CC.RED + "You have nobody to reply to.");
        }
    }

}
