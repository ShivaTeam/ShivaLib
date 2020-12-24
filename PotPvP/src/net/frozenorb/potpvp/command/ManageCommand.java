package net.frozenorb.potpvp.command;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.frozenorb.potpvp.arena.menu.manageschematics.ManageSchematicsMenu;
import net.frozenorb.potpvp.kittype.menu.manage.ManageKitTypeMenu;
import net.frozenorb.potpvp.kittype.menu.select.SelectKitTypeMenu;
import team.shiva.shivalib.menu.Button;
import team.shiva.shivalib.menu.Menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import team.shiva.shivalib.honcho.command.CommandMeta;

import java.util.List;
import java.util.Map;

public final class ManageCommand {

    @CommandMeta(label = {"manage"}, permission = "potpvp.admin")
    public static class Manage{
        public void execute(Player sender){
            manage(sender);
        }
    }
    public static void manage(Player sender) {
        new ManageMenu().openMenu(sender);
    }

    public static class ManageMenu extends Menu {

        public ManageMenu() {
            super("Admin Management Menu");
        }

        @Override
        public Map<Integer, Button> getButtons(Player player) {
            return ImmutableMap.of(
                3, new ManageKitButton(),
                5, new ManageArenaButton()
            );
        }

    }

    private static class ManageKitButton extends Button {

        @Override
        public String getName(Player player) {
            return ChatColor.YELLOW + "Manage kit type definitions";
        }

        @Override
        public List<String> getDescription(Player player) {
            return ImmutableList.of();
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.DIAMOND_SWORD;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            player.closeInventory();

            new SelectKitTypeMenu((kitType) -> {
                player.closeInventory();
                new ManageKitTypeMenu(kitType).openMenu(player);
            }, false, "Manage Kit Type...").openMenu(player);
        }

    }

    private static class ManageArenaButton extends Button {

        @Override
        public String getName(Player player) {
            return ChatColor.YELLOW + "Manage the arena grid";
        }

        @Override
        public List<String> getDescription(Player player) {
            return ImmutableList.of();
        }

        @Override
        public Material getMaterial(Player player) {
            return Material.IRON_PICKAXE;
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType) {
            player.closeInventory();
            new ManageSchematicsMenu().openMenu(player);
        }

    }

}