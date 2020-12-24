package net.frozenorb.potpvp.morpheus.menu;

import com.qrakn.morpheus.game.event.GameEvent;
import team.shiva.shivalib.menu.Button;
import team.shiva.shivalib.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class HostMenu extends Menu {

    public HostMenu() {
        super(ChatColor.DARK_PURPLE + "Host an event");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> toReturn = new HashMap<>();

        for (GameEvent event : GameEvent.getEvents()) {
            toReturn.put(toReturn.size(), new HostEventButton(event));
        }

        return toReturn;
    }

}
