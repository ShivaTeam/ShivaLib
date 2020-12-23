package team.shiva.shivalib.tablist.tab;

import org.bukkit.entity.Player;
import team.shiva.shivalib.ShivaLib;
import team.shiva.shivalib.util.PlayerUtils;

public class DefaultLayoutProvider implements LayoutProvider{
    static final int MAX_TAB_Y = 20;
    @Override
    public TabLayout provide(Player player) {
        TabLayout tabLayout = new TabLayout();
        int x = 0, y = 0;
        for(Player online: ShivaLib.getInstance().getServer().getOnlinePlayers()){
            tabLayout.set(x++, y, online.getDisplayName(), PlayerUtils.getPing(online));
            if(x == 3){
                x = 0;
                y++;
            }
            if(y >= MAX_TAB_Y){
                break;
            }
        }
        return tabLayout;
    }
}
