package net.frozenorb.potpvp.party;

import team.shiva.shivalib.util.ItemBuilder;
import team.shiva.shivalib.util.UUIDUtils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lombok.experimental.UtilityClass;

import static net.frozenorb.potpvp.PotPvPLang.LEFT_ARROW;
import static net.frozenorb.potpvp.PotPvPLang.RIGHT_ARROW;
import static org.bukkit.ChatColor.*;

@UtilityClass
public final class PartyItems {

    public static final Material ICON_TYPE = Material.NETHER_STAR;

    public static final ItemStack LEAVE_PARTY_ITEM = ItemBuilder.of(Material.FIRE).name(RED + "Leave Party").build();
    public static final ItemStack ASSIGN_CLASSES = ItemBuilder.of(Material.ITEM_FRAME).name(GOLD + "HCF Kits").build();
    public static final ItemStack START_TEAM_SPLIT_ITEM = ItemBuilder.of(Material.DIAMOND_SWORD).name(YELLOW + "Start Team Split").build();
    public static final ItemStack START_FFA_ITEM = ItemBuilder.of(Material.GOLD_SWORD).name(YELLOW + "Start Party FFA").build();
    public static final ItemStack OTHER_PARTIES_ITEM = ItemBuilder.of(Material.SKULL_ITEM).name(GREEN + "Other Parties").build();


    public static ItemStack icon(Party party) {
        ItemBuilder item = ItemBuilder.of(ICON_TYPE);

        String leaderName = UUIDUtils.name(party.getLeader());
        String displayName = LEFT_ARROW + AQUA.toString() + BOLD + leaderName + AQUA + "'s Party" + RIGHT_ARROW;

        return item.name(displayName).build();
    }

}
