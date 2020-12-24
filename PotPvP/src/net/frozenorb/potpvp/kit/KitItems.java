package net.frozenorb.potpvp.kit;

import team.shiva.shivalib.util.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.experimental.UtilityClass;

import static net.frozenorb.potpvp.PotPvPLang.LEFT_ARROW;
import static net.frozenorb.potpvp.PotPvPLang.RIGHT_ARROW;
import static org.bukkit.ChatColor.*;

@UtilityClass
public final class KitItems {

    public static final ItemStack OPEN_EDITOR_ITEM = ItemBuilder.of(Material.BOOK).name(AQUA + "Edit Kits").build();

}