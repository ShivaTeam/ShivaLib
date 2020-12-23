package team.shiva.shivalib.scoreboard;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import team.shiva.shivalib.ShivaLib;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ScoreboardHandler {
    private static final Map<String, Board> boards = new ConcurrentHashMap<>();
    @Getter @Setter
    private static ScoreboardConfiguration configuration = null;
    @Getter @Setter
    private static int updateInterval = 2;
    private static boolean initiated;

    private ScoreboardHandler() {
    }

    public static void init() {
        if (ShivaLib.getInstance().getMainConfig().getBoolean("disableScoreboard")) {
            return;
        }
        Preconditions.checkArgument(!initiated, "ScoreboardHandler is already initiated.");
        new ScoreboardThread().start();
        ShivaLib.getInstance().getServer().getPluginManager().registerEvents(new ScoreboardListener(), ShivaLib.getInstance());
        initiated = true;
    }

    protected static void create(Player player) {
        if (configuration != null) {
            boards.put(player.getName(), new Board(player));
        }
    }

    protected static void updateScoreboard(Player player) {
        Board board = boards.get(player.getName());
        if (board != null) {
            board.update();
        }
    }

    protected static void remove(Player player) {
        boards.remove(player.getName());
    }

}

