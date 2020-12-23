package team.shiva.shivalib.scoreboard;

import team.shiva.shivalib.util.TimeUtils;

public interface ScoreFunction<T> {
    ScoreFunction<Float> TIME_FANCY = value -> {
        if (value >= 60.0f) {
            return TimeUtils.formatIntoMMSS(value.intValue());
        }
        return (double)Math.round(10.0 * (double) value) / 10.0 + "s";
    };
    ScoreFunction<Float> TIME_SIMPLE = value -> TimeUtils.formatIntoMMSS(value.intValue());

    String apply(T var1);
}

