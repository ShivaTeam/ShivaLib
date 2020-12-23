package team.shiva.shivalib.scoreboard;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import team.shiva.shivalib.ShivaLib;

import java.util.*;

final class Board {
    private final Player player;
    private final Scoreboard scoreboard;
    private final Objective objective;
    private final Map<String, Integer> displayedScores = new HashMap<>();
    private final Map<String, String> scorePrefixes = new HashMap<>();
    private final Map<String, String> scoreSuffixes = new HashMap<>();
    private final Set<String> sentTeamCreates = new HashSet<>();
    private final StringBuilder separateScoreBuilder = new StringBuilder();
    private final List<String> separateScores = new ArrayList<>();
    private final Set<String> recentlyUpdatedScores = new HashSet<>();
    private final Set<String> usedBaseScores = new HashSet<>();
    private final String[] prefixScoreSuffix = new String[3];
    private final ThreadLocal<ArrayList<String>> localList = ThreadLocal.withInitial(ArrayList::new);

    public Board(Player player) {
        this.player = player;
        this.scoreboard = ShivaLib.getInstance().getServer().getScoreboardManager().getNewScoreboard();
        this.objective = scoreboard.registerNewObjective("ShivaTeam", "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);
    }

    public void update() {
        String untranslatedTitle = ScoreboardHandler.getConfiguration().getTitleGetter().getTitle(this.player);
        String title = ChatColor.translateAlternateColorCodes('&', untranslatedTitle);
        ArrayList<String> lines = localList.get();
        if (!lines.isEmpty()) {
            lines.clear();
        }
        ScoreboardHandler.getConfiguration().getScoreGetter().getScores(lines, this.player);
        this.recentlyUpdatedScores.clear();
        this.usedBaseScores.clear();
        int nextValue = lines.size();
        Preconditions.checkArgument(lines.size() < 16 , "Too many lines passed!");
        Preconditions.checkArgument(title.length() < 32, "Title is too long!");
        if (!this.objective.getDisplayName().equals(title)) {
            this.objective.setDisplayName(title);
        }
        for (String line : lines) {
            if (line.length() >= 45) {
                throw new IllegalArgumentException("Line is too long! Offending line: " + line);
            }
            String[] separated = this.separate(line, this.usedBaseScores);
            String prefix = separated[0];
            String score = separated[1];
            String suffix = separated[2];
            this.recentlyUpdatedScores.add(score);
            if (!this.sentTeamCreates.contains(score)) {
                this.createAndAddMember(score);
            }
            if (!this.displayedScores.containsKey(score) || this.displayedScores.get(score) != nextValue) {
                this.setScore(score, nextValue);
            }
            if (!(this.scorePrefixes.containsKey(score) && this.scorePrefixes.get(score).equals(prefix) && this.scoreSuffixes.get(score).equals(suffix))) {
                this.updateScore(score, prefix, suffix);
            }
            --nextValue;
        }
        for (String displayedScore : ImmutableSet.copyOf(this.displayedScores.keySet())) {
            if (this.recentlyUpdatedScores.contains(displayedScore)) continue;
            this.removeScore(displayedScore);
        }
    }

    private void createAndAddMember(String scoreTitle) {
        Team team = scoreboard.getTeam(scoreTitle);
        if(team == null){
            team = scoreboard.registerNewTeam(scoreTitle);
        }
        if(!team.getEntries().contains(scoreTitle)){
            team.addEntry(scoreTitle);
        }
        this.sentTeamCreates.add(scoreTitle);
    }

    private void setScore(String score, int value) {
        objective.getScore(score).setScore(value);
        this.displayedScores.put(score, value);
    }

    private void removeScore(String score) {
        this.displayedScores.remove(score);
        this.scorePrefixes.remove(score);
        this.scoreSuffixes.remove(score);
        scoreboard.resetScores(score);
    }

    private void updateScore(String score, String prefix, String suffix) {
        this.scorePrefixes.put(score, prefix);
        this.scoreSuffixes.put(score, suffix);
        Team team = scoreboard.getTeam(score);
        team.setPrefix(prefix);
        team.setSuffix(suffix);
    }

    private String[] separate(String line, Collection<String> usedBaseScores) {
        line = ChatColor.translateAlternateColorCodes('&', line);
        String prefix = "";
        String score = "";
        String suffix = "";
        this.separateScores.clear();
        this.separateScoreBuilder.setLength(0);
        for (int i = 0; i < line.length(); ++i) {
            char c = line.charAt(i);
            if (c == '*' || this.separateScoreBuilder.length() == 16 && this.separateScores.size() < 3) {
                this.separateScores.add(this.separateScoreBuilder.toString());
                this.separateScoreBuilder.setLength(0);
                if (c == '*') continue;
            }
            this.separateScoreBuilder.append(c);
        }
        this.separateScores.add(this.separateScoreBuilder.toString());
        switch (this.separateScores.size()) {
            case 1: {
                score = this.separateScores.get(0);
                break;
            }
            case 2: {
                score = this.separateScores.get(0);
                suffix = this.separateScores.get(1);
                break;
            }
            case 3: {
                prefix = this.separateScores.get(0);
                score = this.separateScores.get(1);
                suffix = this.separateScores.get(2);
                break;
            }
            default: {
                ShivaLib.getInstance().getLogger().warning("Failed to separate scoreboard line. Input: " + line);
            }
        }
        if (usedBaseScores.contains(score)) {
            if (score.length() <= 14) {
                for (ChatColor chatColor : ChatColor.values()) {
                    String possibleScore = chatColor + score;
                    if (usedBaseScores.contains(possibleScore)) continue;
                    score = possibleScore;
                    break;
                }
                if (usedBaseScores.contains(score)) {
                    ShivaLib.getInstance().getLogger().warning("Failed to find alternate color code for: " + score);
                }
            } else {
                ShivaLib.getInstance().getLogger().warning("Found a scoreboard base collision to shift: " + score);
            }
        }
        if (prefix.length() > 16) {
            prefix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">16";
        }
        if (score.length() > 16) {
            score = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">16";
        }
        if (suffix.length() > 16) {
            suffix = ChatColor.DARK_RED.toString() + ChatColor.BOLD + ">16";
        }
        usedBaseScores.add(score);
        this.prefixScoreSuffix[0] = prefix;
        this.prefixScoreSuffix[1] = score;
        this.prefixScoreSuffix[2] = suffix;
        return this.prefixScoreSuffix;
    }
}

