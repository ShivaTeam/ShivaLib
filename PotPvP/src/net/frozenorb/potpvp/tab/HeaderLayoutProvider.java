package net.frozenorb.potpvp.tab;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.tablist.tab.TabLayout;
import team.shiva.shivalib.util.PlayerUtils;

import java.util.function.BiConsumer;

final class HeaderLayoutProvider implements BiConsumer<Player, TabLayout> {

    @Override
    public void accept(Player player, TabLayout tabLayout) {
        header: {
            tabLayout.set(1, 0, "&6&lMineHQ PotPvP");
        }

        status: {
            tabLayout.set(1, 1, ChatColor.GRAY + "Your Connection", Math.max(((PlayerUtils.getPing(player) + 5) / 10) * 10, 1));
        }
    }

}
