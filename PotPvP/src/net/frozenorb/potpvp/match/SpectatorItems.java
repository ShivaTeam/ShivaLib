package net.frozenorb.potpvp.match;

import team.shiva.shivalib.util.ItemBuilder;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class SpectatorItems {

    public static final ItemStack SHOW_SPECTATORS_ITEM = ItemBuilder.of(Material.INK_SACK, 1).data(DyeColor.GRAY.getDyeData()).name(ChatColor.YELLOW + "Show spectators").build();
    public static final ItemStack HIDE_SPECTATORS_ITEM = ItemBuilder.of(Material.INK_SACK, 1).data(DyeColor.LIME.getDyeData()).name(ChatColor.YELLOW + "Hide spectators").build();

    public static final ItemStack VIEW_INVENTORY_ITEM = ItemBuilder.of(Material.BOOK).name(ChatColor.YELLOW + "View player inventory").build();

    // these items both do the same thing but we change the name if
    // clicking the item will reuslt in the player being removed
    // from their party. both serve the function of returning a player
    // to the lobby.
    // https://github.com/FrozenOrb/PotPvP-SI/issues/37

    public static final ItemStack RETURN_TO_LOBBY_ITEM = ItemBuilder.of(Material.FIRE).name(ChatColor.YELLOW + "Return to lobby").build();
    public static final ItemStack LEAVE_PARTY_ITEM = ItemBuilder.of(Material.FIRE).name(ChatColor.YELLOW + "Leave party").build();

}