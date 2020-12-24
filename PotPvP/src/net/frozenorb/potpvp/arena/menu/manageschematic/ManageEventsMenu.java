package net.frozenorb.potpvp.arena.menu.manageschematic;

import com.qrakn.morpheus.game.event.GameEvent;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.arena.ArenaSchematic;
import net.frozenorb.potpvp.arena.menu.manageschematics.ManageSchematicsMenu;
import net.frozenorb.potpvp.util.menu.BooleanTraitButton;
import net.frozenorb.potpvp.util.menu.IntegerTraitButton;
import net.frozenorb.potpvp.util.menu.MenuBackButton;
import team.shiva.shivalib.menu.Button;
import team.shiva.shivalib.menu.Menu;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.*;
import java.util.function.Consumer;

public final class ManageEventsMenu extends Menu {

    private final ArenaSchematic schematic;

    public ManageEventsMenu(ArenaSchematic schematic) {
        super("Manage " + schematic.getName() + "Events");
        setAutoUpdate(true);

        this.schematic = schematic;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (GameEvent event : GameEvent.getEvents()) {
            buttons.put(buttons.size(), new Button() {
                @Override
                public String getName(Player player) {
                    if (schematic.getEvent() == event) {
                        return ChatColor.GREEN + event.getName();
                    } else {
                        return ChatColor.RED + event.getName();
                    }
                }

                @Override
                public List<String> getDescription(Player player) {
                    List<String> toReturn = new ArrayList<>();

                    if (schematic.getEvent() != event) {
                        toReturn.add(ChatColor.GRAY + "Click to lock this arena to the " + event.getName() + " event.");
                    } else {
                        toReturn.add(ChatColor.GRAY + "Click to unbind this arena from the " + event.getName() + " event.");
                    }

                    return toReturn;
                }

                @Override
                public Material getMaterial(Player player) {
                    return event.getIcon().getType();
                }

                @Override
                public void clicked(Player player, int slot, ClickType clickType) {
                    if (schematic.getEvent() != event) {
                        schematic.setArcherOnly(false);
                        schematic.setBuildUHCOnly(false);
                        schematic.setHCFOnly(false);
                        schematic.setSpleefOnly(false);
                        schematic.setSupportsRanked(false);

                        schematic.setEventName(event.getName());
                    } else {
                        schematic.setEventName(null);
                    }
                }
            });
        }

        return buttons;
    }

}