package net.frozenorb.potpvp.setting.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.setting.Setting;
import net.frozenorb.potpvp.setting.SettingHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

/**
 * /night command, allows players to toggle {@link Setting#NIGHT_MODE} setting
 */
public final class NightCommand {

    @CommandMeta(label = { "night", "nightMode" }, permission = "")
    public static class Night{
        public void execute(Player sender){
            night(sender);
        }
    }
    public static void night(Player sender) {
        if (!Setting.NIGHT_MODE.canUpdate(sender)) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return;
        }

        SettingHandler settingHandler = PotPvPSI.getInstance().getSettingHandler();
        boolean enabled = !settingHandler.getSetting(sender, Setting.NIGHT_MODE);

        settingHandler.updateSetting(sender, Setting.NIGHT_MODE, enabled);

        if (enabled) {
            sender.sendMessage(ChatColor.GREEN + "Night mode on.");
        } else {
            sender.sendMessage(ChatColor.RED + "Night mode off.");
        }
    }

}