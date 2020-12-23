package team.shiva.shivalib.tablist.tab;

import team.shiva.shivalib.tablist.abstraction.EntityPlayerWrapper;
import org.bukkit.scoreboard.Team;

public interface StandardRow
{

    void setRowTeam(final Team p0);

    String getRowString();

    int getPing();

    void setRowPlayer(final EntityPlayerWrapper p0);

    @Override
    boolean equals(Object o);

    default int pingToBars(int ping) {
        if (ping < 0) {
            return 5;
        }
        if (ping < 150) {
            return 0;
        }
        if (ping < 300) {
            return 1;
        }
        if (ping < 600) {
            return 2;
        }
        if (ping < 1000) {
            return 3;
        }
        if (ping < 32767) {
            return 4;
        }
        return 5;
    }
}
