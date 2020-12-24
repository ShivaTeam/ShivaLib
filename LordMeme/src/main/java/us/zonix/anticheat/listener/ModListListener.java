package us.zonix.anticheat.listener;

import org.bukkit.ChatColor;
import us.zonix.anticheat.*;
import us.zonix.anticheat.client.*;
import us.zonix.anticheat.event.ModListRetrieveEvent;
import us.zonix.anticheat.event.player.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.data.*;
import java.util.*;
import org.bukkit.event.*;
import com.google.common.collect.*;

public class ModListListener implements Listener
{
    private static final Map<String, String> BLACKLISTED_MODS;
    private final LordMeme plugin;
    
    @EventHandler
    public void onModListRetrieve(final ModListRetrieveEvent event) {
        final Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        final PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
        if (playerData == null) {
            return;
        }
        final Map<String, String> mods = (Map<String, String>)event.getMods();
        EnumClientType type;
        if (mods.containsKey("gc")) {
            type = EnumClientType.HACKED_CLIENT_F;
        }
        else if ("1.0".equals(mods.get("OpenComputers"))) {
            type = EnumClientType.HACKED_CLIENT_G;
        }
        else if (mods.containsKey("ethylene")) {
            type = EnumClientType.HACKED_CLIENT_H;
        }
        else if ("1.7.6.git".equals(mods.get("Schematica"))) {
            type = EnumClientType.HACKED_CLIENT_I;
        }
        else if (mods.containsKey("Aimbot")) {
            type = EnumClientType.HACKED_CLIENT_J;
        }
        else if ("1.0 ".equals(mods.get("timechanger"))) {
            type = EnumClientType.HACKED_CLIENT_E2;
        }
        else if ("1.0".equals(mods.get("TcpNoDelayMod-2.0"))) {
            type = EnumClientType.HACKED_CLIENT_K;
        }
        else if (mods.containsKey("mergeclient")) {
            type = EnumClientType.HACKED_CLIENT_L;
        }
        else if (mods.containsKey("wigger")) {
            type = EnumClientType.HACKED_CLIENT_L2;
        }
        else {
            type = EnumClientType.FORGE;
            final StringJoiner blacklisted = new StringJoiner(", ");
            boolean kick = false;
            for (final String modId : ModListListener.BLACKLISTED_MODS.keySet()) {
                if (mods.containsKey(modId)) {
                    blacklisted.add(ModListListener.BLACKLISTED_MODS.get(modId));
                    kick = true;
                }
            }
            if (kick) {
                player.kickPlayer(ChatColor.RED + "Please contact an administrator.");
            }
        }
        playerData.setClient(type);
        playerData.setForgeMods(mods);
        if (type.isHacked()) {
            playerData.setRandomBanRate(500.0);
            playerData.setRandomBanReason(type.getName());
            playerData.setRandomBan(true);
            final PlayerAlertEvent alertEvent = new PlayerAlertEvent(PlayerAlertEvent.AlertType.RELEASE, player, ChatColor.YELLOW + "was caught using a " + ChatColor.GOLD + type.getName() + ".");
            this.plugin.getServer().getPluginManager().callEvent((Event)alertEvent);
        }
    }
    
    public ModListListener(final LordMeme plugin) {
        this.plugin = plugin;
    }
    
    static {
        BLACKLISTED_MODS = (Map)ImmutableMap.of((Object)"MouseTweaks", (Object)"Mouse Tweaks", (Object)"Particle Mod", (Object)"Particle Mod", (Object)"npcmod", (Object)"NPC Mod");
    }
}
