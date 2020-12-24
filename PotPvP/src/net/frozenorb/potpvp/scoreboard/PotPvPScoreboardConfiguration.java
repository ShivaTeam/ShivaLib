package net.frozenorb.potpvp.scoreboard;

import team.shiva.shivalib.scoreboard.ScoreboardConfiguration;
import team.shiva.shivalib.scoreboard.TitleGetter;

import org.apache.commons.lang.StringEscapeUtils;

public final class PotPvPScoreboardConfiguration {

    public static ScoreboardConfiguration create() {
        ScoreboardConfiguration configuration = new ScoreboardConfiguration();

        configuration.setTitleGetter(TitleGetter.forStaticString("&6&lMineHQ &7" + StringEscapeUtils.unescapeJava("\u2758") +" &fPotPvP"));
        configuration.setScoreGetter(new MultiplexingScoreGetter(
            new MatchScoreGetter(),
            new LobbyScoreGetter(),
            new GameScoreGetter()
        ));

        return configuration;
    }

}
