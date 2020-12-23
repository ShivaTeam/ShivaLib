package team.shiva.shivalib.scoreboard;

import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface ScoreGetter {
    public void getScores(ArrayList<String> result, Player player);
}

