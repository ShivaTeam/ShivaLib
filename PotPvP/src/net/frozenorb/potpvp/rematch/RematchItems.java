package net.frozenorb.potpvp.rematch;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import team.shiva.shivalib.util.ItemBuilder;

import static org.bukkit.ChatColor.DARK_PURPLE;
import static org.bukkit.ChatColor.GREEN;

@UtilityClass
public final class RematchItems {

    public static final ItemStack REQUEST_REMATCH_ITEM = ItemBuilder.of(Material.DIAMOND).name(DARK_PURPLE + "Request Rematch").build();
    public static final ItemStack SENT_REMATCH_ITEM = ItemBuilder.of(Material.DIAMOND).name(GREEN + "Sent Rematch").build();
    public static final ItemStack ACCEPT_REMATCH_ITEM = ItemBuilder.of(Material.DIAMOND).name(GREEN + "Accept Rematch").build();

}