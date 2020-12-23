package team.shiva.shivalib.tablist;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import team.shiva.shivalib.ShivaLib;
import team.shiva.shivalib.tablist.abstraction.PacketUtil;
import team.shiva.shivalib.tablist.abstraction.PacketUtil8;
import team.shiva.shivalib.tablist.tab.*;
import us.myles.ViaVersion.api.Via;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TablistHandler implements Listener {


    private static final Map<Player, Tab> tabList = new HashMap<>();
    @Getter private static PacketUtil packetUtil;
    @Getter private static boolean initialed;
    @Getter @Setter private static String tabTitle = "";
    @Getter @Setter private static String tabFooter = "";
    @Setter private static LayoutProvider layoutProvider = new DefaultLayoutProvider();

    public static void init(String title, String footer){
        init();
        tabTitle = title;
        tabFooter = footer;
    }

    public static void init() {
        Preconditions.checkArgument(!initialed, "TablistHandler is already initiated.");
        packetUtil = new PacketUtil8();
        ShivaLib.getInstance().getServer().getPluginManager().registerEvents(new TablistListener(), ShivaLib.getInstance());
        ShivaLib.getInstance().getHoncho().registerCommand(new ResetTablistCommand());
        new TablistThread().start();
        initialed = true;
    }

    private static boolean hasTab(Player player) {
        return tabList.containsKey(player);
    }

    public static void addPlayer(Player player){
        if(!hasTab(player)){
            tabList.put(player, new Tab(player, tabTitle, tabFooter));
        }
    }

    public static void removePlayer(Player player){
        if(hasTab(player)){
            tabList.remove(player);
        }
    }

    public static void updatePlayer(Player player){
        if(hasTab(player)){
            Tab tab = tabList.get(player);
            TabLayout tabLayout = layoutProvider.provide(player);
            List<StandardRow> standardRows = tabLayout.getStandardRows();
            if(tab.isDifferentFrom(standardRows)){
                tab.clearTab();
                for(StandardRow standardRow: standardRows){
                    tab.addTabRow(standardRow);
                }
                if(Via.getAPI().getPlayerVersion(player.getUniqueId()) > 20){
                    tab.sendTab();
                }else{
                    tab.sendTab7();
                }
            }
        }
    }

}
