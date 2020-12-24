package team.shiva.core.profile.staff.command;

import team.shiva.core.Core;
import team.shiva.core.CoreAPI;
import team.shiva.core.network.packet.PacketStaffChat;
import team.shiva.core.profile.Profile;
import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandMeta(label = { "staffchat", "sc" }, permission = "zoot.staff")
public class StaffChatCommand {

	public void execute(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.getStaffOptions().staffChatModeEnabled(!profile.getStaffOptions().staffChatModeEnabled());

		player.sendMessage(profile.getStaffOptions().staffChatModeEnabled() ?
				CC.GREEN + "You are now talking in staff chat." : CC.RED + "You are no longer talking in staff chat.");
	}

	public void execute(Player player, String message) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());

		if (!profile.getStaffOptions().staffModeEnabled()) {
			player.sendMessage(CC.RED + "You are not in staff mode.");
			return;
		}

		Core.get().getPidgin().sendPacket(new PacketStaffChat(CoreAPI.getColoredName(player),
				Bukkit.getServerId(), message));
	}

}
