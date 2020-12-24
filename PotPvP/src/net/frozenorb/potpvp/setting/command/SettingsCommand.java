package net.frozenorb.potpvp.setting.command;

import net.frozenorb.potpvp.setting.menu.SettingsMenu;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

/**
 * /settings, accessible by all users, opens a {@link SettingsMenu}
 */
public final class SettingsCommand {

    @CommandMeta(label = {"settings", "preferences", "prefs", "options"}, permission = "")
    public static class Settings{
        public void execute(Player sender){
            settings(sender);
        }
    }
    public static void settings(Player sender) {
        new SettingsMenu().openMenu(sender);
    }

}