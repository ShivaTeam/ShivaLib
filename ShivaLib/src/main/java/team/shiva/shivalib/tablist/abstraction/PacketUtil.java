package team.shiva.shivalib.tablist.abstraction;

import team.shiva.shivalib.tablist.util.TabIcon;
import org.bukkit.entity.*;

public interface PacketUtil
{
    EntityPlayerWrapper newRow(final String p0, final TabIcon p1, int ping);

    void sendRows(final Player p0, final EntityPlayerWrapper[] p1);

    void clearRows(final Player p0, final EntityPlayerWrapper[] p1);

    void clearTab(final Player p0);

    void setFooterAndHeader(final Player p0, final String p1, final String p2);
}
