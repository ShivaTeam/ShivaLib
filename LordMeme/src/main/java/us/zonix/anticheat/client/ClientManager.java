package us.zonix.anticheat.client;

import org.bukkit.ChatColor;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import java.util.*;
import us.zonix.anticheat.event.player.*;
import org.bukkit.event.*;
import com.google.common.collect.*;

public class ClientManager
{
    private static final Map<String, String> BLACKLISTED_MODS;
    private final Set<PayloadClientType> payloadClients;
    private final Set<ModClientType> modClients;
    private final LordMeme plugin;
    
    public ClientManager(final LordMeme plugin) {
        this.payloadClients = new HashSet<>();
        this.modClients = new HashSet<>();
        this.plugin = plugin;
        this.modClients.add(new ModClientType("Ethylene", "ethylene", null));
        this.modClients.add(new ModClientType("Ghost Client (Generic)", "gc", null));
        this.modClients.add(new ModClientType("Merge Aimbot", "Aimbot", null));
        this.modClients.add(new ModClientType("Cracked Vape v2.49", "mergeclient", null));
        this.modClients.add(new ModClientType("Cracked Vape v2.50", "wigger", null));
        this.modClients.add(new ModClientType("OpenComputers", "OpenComputers", "1.0"));
        this.modClients.add(new ModClientType("Schematica Reach", "Schematica", "1.7.6.git"));
        this.modClients.add(new ModClientType("TimeChanger Misplace", "timechanger", "1.0 "));
        this.modClients.add(new ModClientType("TcpNoDelay Clients", "TcpNoDelayMod-2.0", "1.0"));
        this.payloadClients.add(new PayloadClientType("Cracked Vape v2.06", "LOLIMAHCKER", true));
        this.payloadClients.add(new PayloadClientType("Cracked Merge", "cock", true));
        this.payloadClients.add(new PayloadClientType("BspkrsCore Client 1", "customGuiOpenBspkrs", true));
        this.payloadClients.add(new PayloadClientType("BspkrsCore Client 2", "0SO1Lk2KASxzsd", true));
        this.payloadClients.add(new PayloadClientType("BspkrsCore Client 3", "mincraftpvphcker", true));
        this.payloadClients.add(new PayloadClientType("Cracked Incognito", "lmaohax", true));
        this.payloadClients.add(new PayloadClientType("Old TimeChanger Misplace", "MCnetHandler", true));
        this.payloadClients.add(new PayloadClientType("OCMC", "OCMC", false));
        this.payloadClients.add(new PayloadClientType("CheatBreaker", "CB-Client", false));
        this.payloadClients.add(new PayloadClientType("Cosmic Client", "CC", false));
        this.payloadClients.add(new PayloadClientType("Labymod", "LABYMOD", false));
    }
    
    public void onModList(final PlayerData playerData, final Player player, final Map<String, String> mods) {
        this.modClients.forEach(clientType -> {
            if (clientType.getModVersion() == null) {
                if (mods.containsKey(clientType.getModId())) {}
            }
            else if (clientType.getModVersion().equals(mods.get(clientType.getModId()))) {}
            return;
        });
        if (!playerData.getClient().isHacked()) {
            playerData.setClient(EnumClientType.FORGE);
        }
        this.checkCheats(playerData, player);
        final StringJoiner blacklisted = new StringJoiner(", ");
        boolean kick = false;
        for (final String modId : ClientManager.BLACKLISTED_MODS.keySet()) {
            if (mods.containsKey(modId)) {
                blacklisted.add(ClientManager.BLACKLISTED_MODS.get(modId));
                kick = true;
            }
        }
        if (kick) {
            player.kickPlayer(ChatColor.RED + "Please contact an administrator.");
            //player.kickPlayer(ChatColor.RED + "[LordMeme] Unrestricted Forge Mod" + (blacklisted.toString().contains(", ") ? "s" : "") + " Detected.\nPlease remove the following mods:\n" + blacklisted.toString());
        }
    }
    
    private void checkCheats(final PlayerData playerData, final Player player) {
        if (playerData.getClient().isHacked()) {
            final PlayerAlertEvent event = new PlayerAlertEvent(PlayerAlertEvent.AlertType.RELEASE, player, ChatColor.YELLOW + "was caught using a " + ChatColor.GOLD + playerData.getClient().getName() + ".");
            this.plugin.getServer().getPluginManager().callEvent((Event)event);
            playerData.setRandomBanReason(playerData.getClient().getName());
            playerData.setRandomBanRate(500.0);
            playerData.setRandomBan(true);
        }
    }
    
    static {
        BLACKLISTED_MODS = (Map)ImmutableMap.of((Object)"MouseTweaks", (Object)"Mouse Tweaks", (Object)"Particle Mod", (Object)"Particle Mod");
    }
}
