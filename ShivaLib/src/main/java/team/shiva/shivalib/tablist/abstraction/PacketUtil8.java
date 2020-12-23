package team.shiva.shivalib.tablist.abstraction;

import team.shiva.shivalib.tablist.util.TabIcon;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import java.util.*;
import org.bukkit.entity.*;
import java.lang.reflect.*;
import net.minecraft.server.v1_8_R3.*;

public class PacketUtil8 implements PacketUtil
{
    public EntityPlayer[] getOnlineCraftPlayers() {
        final Collection<? extends Player> onlinePlayers = (Collection<? extends Player>)Bukkit.getOnlinePlayers();
        final EntityPlayer[] NMSList = new EntityPlayer[onlinePlayers.size()];
        for (int i = 0; i < onlinePlayers.size(); ++i) {
            NMSList[i] = ((CraftPlayer)onlinePlayers.toArray()[i]).getHandle();
        }
        return NMSList;
    }

    @Override
    public void setFooterAndHeader(final Player player, final String header, final String footer) {
        if (header != null && footer != null) {
            final PacketPlayOutPlayerListHeaderFooter playerListHeaderFooter = new PacketPlayOutPlayerListHeaderFooter();
            try {
                final Field headerField = playerListHeaderFooter.getClass().getDeclaredField("a");
                final Field footerField = playerListHeaderFooter.getClass().getDeclaredField("b");
                headerField.setAccessible(true);
                headerField.set(playerListHeaderFooter, new ChatMessage(header, new Object[0]));
                headerField.setAccessible(false);
                footerField.setAccessible(true);
                footerField.set(playerListHeaderFooter, new ChatMessage(footer, new Object[0]));
                footerField.setAccessible(false);
            }
            catch (NoSuchFieldException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)playerListHeaderFooter);
        }
    }

    private EntityPlayer[] entityPlayers(final EntityPlayerWrapper[] entityPlayerWrappers) {
        final EntityPlayer[] entityPlayers = new EntityPlayer[entityPlayerWrappers.length];
        for (int i = 0; i < entityPlayerWrappers.length; ++i) {
            entityPlayers[i] = ((EntityPlayer8)entityPlayerWrappers[i]).getEntityPlayer();
        }
        return entityPlayers;
    }

    @Override
    public void clearTab(final Player player) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.getOnlineCraftPlayers()));
    }

    @Override
    public void clearRows(final Player player, final EntityPlayerWrapper[] tabRows) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entityPlayers(tabRows)));
    }

    @Override
    public void sendRows(final Player player, final EntityPlayerWrapper[] tabRows) {
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket((Packet)new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.entityPlayers(tabRows)));
    }

    @Override
    public EntityPlayerWrapper newRow(final String rowString, final TabIcon tabIcon, int ping) {
        return new EntityPlayer8(rowString, tabIcon, ping);
    }
}