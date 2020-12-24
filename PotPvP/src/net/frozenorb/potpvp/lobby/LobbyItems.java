package net.frozenorb.potpvp.lobby;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import team.shiva.shivalib.util.ItemBuilder;

import static net.frozenorb.potpvp.PotPvPLang.LEFT_ARROW;
import static net.frozenorb.potpvp.PotPvPLang.RIGHT_ARROW;
import static org.bukkit.ChatColor.*;

@UtilityClass
public final class LobbyItems {

    public static final ItemStack SPECTATE_RANDOM_ITEM = ItemBuilder.of(Material.COMPASS).name(LEFT_ARROW + YELLOW.toString() + BOLD + "Spectate Random Match" + RIGHT_ARROW).build();
    public static final ItemStack SPECTATE_MENU_ITEM = ItemBuilder.of(Material.PAPER).name(LEFT_ARROW + GREEN.toString() + BOLD + "Spectate Menu" + RIGHT_ARROW).build();
    public static final ItemStack ENABLE_SPEC_MODE_ITEM = ItemBuilder.of(Material.REDSTONE_TORCH_ON).name(LEFT_ARROW + AQUA.toString() + BOLD + "Enable Spectator Mode" + RIGHT_ARROW).build();
    public static final ItemStack DISABLE_SPEC_MODE_ITEM = ItemBuilder.of(Material.LEVER).name(LEFT_ARROW + AQUA.toString() + BOLD + "Disable Spectator Mode" + RIGHT_ARROW).build();
    public static final ItemStack MANAGE_ITEM = ItemBuilder.of(Material.ANVIL).name(RED + "Manage PotPvP").build();
    public static final ItemStack UNFOLLOW_ITEM = ItemBuilder.of(Material.INK_SACK, 1).data(DyeColor.RED.getDyeData()).name(LEFT_ARROW + RED + BOLD.toString() + "Stop Following" + RIGHT_ARROW).build();
    public static final ItemStack PLAYER_STATISTICS = ItemBuilder.of(Material.SKULL_ITEM, 1).data((short) 3).name(LEFT_ARROW + ChatColor.LIGHT_PURPLE.toString() + BOLD + "Statistics" + RIGHT_ARROW).build();

}