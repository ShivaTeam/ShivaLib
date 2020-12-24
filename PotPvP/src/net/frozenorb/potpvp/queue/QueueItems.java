package net.frozenorb.potpvp.queue;

import team.shiva.shivalib.util.ItemBuilder;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.experimental.UtilityClass;

import static net.frozenorb.potpvp.PotPvPLang.LEFT_ARROW;
import static net.frozenorb.potpvp.PotPvPLang.RIGHT_ARROW;
import static org.bukkit.ChatColor.*;

@UtilityClass
public final class QueueItems {

    public static final ItemStack JOIN_SOLO_UNRANKED_QUEUE_ITEM = ItemBuilder.of(Material.IRON_SWORD).name(YELLOW + "Play Unranked").build();
    public static final ItemStack LEAVE_SOLO_UNRANKED_QUEUE_ITEM = ItemBuilder.of(Material.INK_SACK, 1).data((byte) DyeColor.RED.getDyeData()).name(RED + "Leave Unranked Queue").build();

    public static final ItemStack JOIN_SOLO_RANKED_QUEUE_ITEM = ItemBuilder.of(Material.DIAMOND_SWORD).name(GOLD + "Play Ranked").build();
    public static final ItemStack LEAVE_SOLO_RANKED_QUEUE_ITEM = ItemBuilder.of(Material.INK_SACK, 1).data((byte) DyeColor.RED.getDyeData()).name(RED + "Leave Ranked Queue").build();

    public static final ItemStack JOIN_PARTY_UNRANKED_QUEUE_ITEM = ItemBuilder.of(Material.IRON_SWORD).name(BLUE + "Play 2v2 Unranked").build();
    public static final ItemStack LEAVE_PARTY_UNRANKED_QUEUE_ITEM = ItemBuilder.of(Material.ARROW).name(RED + "Leave 2v2 Unranked Queue").build();

    public static final ItemStack JOIN_PARTY_RANKED_QUEUE_ITEM = ItemBuilder.of(Material.DIAMOND_SWORD).name(DARK_AQUA + "Join 2v2 Ranked").build();
    public static final ItemStack LEAVE_PARTY_RANKED_QUEUE_ITEM = ItemBuilder.of(Material.ARROW).name(RED + "Leave 2v2 Ranked Queue").build();


}