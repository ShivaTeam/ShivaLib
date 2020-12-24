package net.frozenorb.potpvp.kittype.menu.select;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import com.google.common.collect.ImmutableList;

import lombok.AllArgsConstructor;
import team.shiva.shivalib.menu.Button;

@AllArgsConstructor
public class ToggleAllButton extends Button {

    private Set<String> allMaps;
    private Set<String> maps;
    
    @Override
    public List<String> getDescription(Player arg0) {
        return ImmutableList.of();
    }

    @Override
    public Material getMaterial(Player arg0) {
        return maps.isEmpty() ? Material.REDSTONE_TORCH_ON : Material.REDSTONE_TORCH_OFF;
    }

    @Override
    public String getName(Player arg0) {
        return maps.isEmpty() ? ChatColor.GREEN + "Enable all maps" : ChatColor.RED + "Disable all maps";
    }
    
    @Override
    public void clicked(Player player, int slot, ClickType clickType) {
        if (maps.isEmpty()) {
            maps.addAll(allMaps);
        } else {
            maps.clear();
        }
    }
}
