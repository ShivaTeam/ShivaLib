package team.shiva.shivalib.nametag;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import team.shiva.shivalib.ShivaLib;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NametagHandler {
    private static final String PREFIX = "nt_team_";
    @Getter
    private static boolean initiated = false;
    @Getter @Setter
    private static boolean async = true;
    @Getter @Setter
    private static int updateInterval = 2;
    private static final List<NametagProvider> providers = new ArrayList<>();

    public static void init() {
        if (ShivaLib.getInstance().getMainConfig().getBoolean("disableNametags")) {
            return;
        }
        Preconditions.checkState(!initiated,"NametagHandler is already initiated.");
        ShivaLib.getInstance().getServer().getPluginManager().registerEvents(new NametagListener(), ShivaLib.getInstance());
        registerProvider(new NametagProvider.DefaultNametagProvider());
        initiated = true;
    }

    public static void registerProvider(NametagProvider newProvider) {
        providers.add(newProvider);
        providers.sort(new Comparator<NametagProvider>(){
            @Override
            public int compare(NametagProvider a, NametagProvider b) {
                return Ints.compare(b.getWeight(), a.getWeight());
            }
        });
    }

    public static void color(Player player, Player other, ChatColor color, boolean showHealth) {
        Scoreboard scoreboard = player.getScoreboard();

        if (scoreboard.equals(Bukkit.getServer().getScoreboardManager().getMainScoreboard())) {
            scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        }

        Team team = player.getScoreboard().getTeam(getTeamName(color));

        if (team == null) {
            team = player.getScoreboard().registerNewTeam(getTeamName(color));
            team.setPrefix(color.toString());
        }

        if (!team.hasEntry(other.getName())) {
            reset(player, other);

            team.addEntry(other.getName());

            if (showHealth) {
                Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

                if (objective == null) {
                    objective = player.getScoreboard().registerNewObjective("showhealth", "health");
                }

                objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
                objective.setDisplayName(ChatColor.RED + StringEscapeUtils.unescapeJava("\u2764"));
                objective.getScore(other.getName()).setScore((int) Math.floor(other.getHealth()));
            }
        }

        player.setScoreboard(scoreboard);
    }

    public static void reset(Player player, Player other) {
        if (player != null && other != null && !player.equals(other)) {
            Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

            if (objective != null) {
                objective.unregister();
            }

            for (ChatColor chatColor : ChatColor.values()) {
                Team team = player.getScoreboard().getTeam(getTeamName(chatColor));

                if (team != null) {
                    team.removeEntry(other.getName());
                }
            }
        }
    }

    public static void update(Player player, Player other, NametagInfo info){
        if(info.color == null){
            reset(player, other);
        }else{
            color(player, other, info.color, info.showHealth);
        }
    }

    private static String getTeamName(ChatColor color) {
        return PREFIX + color.ordinal();
    }

    public static void reloadPlayer(Player player) {
        for(Player other: ShivaLib.getInstance().getServer().getOnlinePlayers()){
            reloadPlayer(player ,other);
        }
    }

    public static void reloadOthersFor(Player player) {
        for(Player other: ShivaLib.getInstance().getServer().getOnlinePlayers()){
            reloadPlayer(other ,player);
        }
    }

    public static void reloadPlayer(Player toRefresh, Player refreshFor) {
        if (!refreshFor.hasMetadata("ShivaLib_nt")) {
            return;
        }
        NametagInfo provided = null;
        int providerIndex = 0;
        while (provided == null) {
            provided = providers.get(providerIndex++).fetchNametag(toRefresh, refreshFor);
        }
        update(refreshFor, toRefresh, provided);
    }
}
