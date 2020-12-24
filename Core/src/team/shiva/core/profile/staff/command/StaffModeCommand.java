package team.shiva.core.profile.staff.command;

import team.shiva.core.profile.Profile;
import team.shiva.core.util.CC;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = { "staffmode", "sm" }, permission = "zoot.staff")
public class StaffModeCommand {

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.getStaffOptions().staffModeEnabled(!profile.getStaffOptions().staffModeEnabled());

        player.sendMessage(profile.getStaffOptions().staffModeEnabled() ?
                CC.GREEN + "You are now in staff mode." : CC.RED + "You are no longer in staff mode.");
    }

}
