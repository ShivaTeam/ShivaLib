package team.shiva.core.profile.conversation.command;

import team.shiva.core.profile.Profile;
import team.shiva.core.profile.conversation.Conversation;
import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CommandMeta;

import org.bukkit.entity.Player;

@CommandMeta(label = { "message", "msg", "whisper", "tell", "t" })
public class MessageCommand {

    public void execute(Player player, Player target, String message) {
        if (player.equals(target)) {
            player.sendMessage(CC.RED + "You cannot message yourself!");
            return;
        }

        if (target == null) {
            player.sendMessage(CC.RED + "A player with that name could not be found.");
            return;
        }

        Profile playerProfile = Profile.getByUuid(player.getUniqueId());
        Profile targetProfile = Profile.getByUuid(target.getUniqueId());

        if (targetProfile.getConversations().canBeMessagedBy(player)) {
            Conversation conversation = playerProfile.getConversations().getOrCreateConversation(target);

            if (conversation.validate()) {
                conversation.sendMessage(player, target, message);
            } else {
                player.sendMessage(CC.RED + "That player is not receiving new conversations right now.");
            }
        } else {
            player.sendMessage(CC.RED + "That player is not receiving new conversations right now.");
        }
    }

}
