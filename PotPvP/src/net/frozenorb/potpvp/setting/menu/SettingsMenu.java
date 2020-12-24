package net.frozenorb.potpvp.setting.menu;

import lombok.SneakyThrows;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.setting.Setting;
import org.bukkit.plugin.Plugin;
import team.shiva.shivalib.menu.Button;
import team.shiva.shivalib.menu.Menu;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Menu used by /settings to let players toggle settings
 */
public final class SettingsMenu extends Menu {

    boolean hasCore;

    Class publicChatOptionButton;
    Class privateChatOptionButton;
    Class privateChatSoundsOptionButton;

    @SneakyThrows
    public SettingsMenu() {
        super("Edit settings");
        if(PotPvPSI.getInstance().getServer().getPluginManager().getPlugin("Core") != null){
            hasCore = true;
            publicChatOptionButton = Class.forName("team.shiva.core.profile.option.button.PublicChatOptionButton");
            privateChatOptionButton = Class.forName("team.shiva.core.profile.option.button.PrivateChatOptionButton");
            privateChatSoundsOptionButton = Class.forName("team.shiva.core.profile.option.button.PrivateChatSoundsOptionButton");
        }

        setAutoUpdate(true);
    }

    @SneakyThrows
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int index = 0;

        for (Setting setting : Setting.values()) {
            if (setting.canUpdate(player)) {
                buttons.put(index++, new SettingButton(setting));
            }
        }

        if(hasCore){
            buttons.put(index++, (Button)publicChatOptionButton.newInstance());
            buttons.put(index++, (Button)privateChatOptionButton.newInstance());
            buttons.put(index++, (Button)privateChatSoundsOptionButton.newInstance());
        }

        return buttons;
    }

}