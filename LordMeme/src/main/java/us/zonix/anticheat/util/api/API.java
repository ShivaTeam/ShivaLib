package us.zonix.anticheat.util.api;

import org.bukkit.entity.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.client.*;
import us.zonix.anticheat.data.*;

public final class API
{
    public static boolean isCheatBreaker(final Player player) {
        final PlayerData playerData = LordMeme.getInstance().getPlayerDataManager().getPlayerData(player);
        return playerData != null && playerData.getClient() == EnumClientType.CHEAT_BREAKER;
    }
    
    public static int getPing(final Player player) {
        final PlayerData playerData = LordMeme.getInstance().getPlayerDataManager().getPlayerData(player);
        if (playerData != null) {
            return (int)playerData.getPing();
        }
        return 0;
    }
}
