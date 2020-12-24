package us.zonix.anticheat.listener;

import us.zonix.anticheat.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import io.netty.buffer.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.data.*;
import org.bukkit.event.player.*;
import net.minecraft.server.v1_8_R3.*;
import java.text.*;

import org.bukkit.*;
import us.zonix.anticheat.log.*;
import org.bukkit.command.*;
import org.bukkit.event.*;
import us.zonix.anticheat.runnable.*;
import java.util.*;
import us.zonix.anticheat.event.player.*;
import us.zonix.anticheat.util.UtilActionMessage;

public class PlayerListener implements Listener
{
    private final LordMeme plugin;
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        this.plugin.getPlayerDataManager().addPlayerData(event.getPlayer());

        this.plugin.getServer().getScheduler().runTaskLaterAsynchronously((Plugin)this.plugin, () -> {

            final PlayerConnection playerConnection = ((CraftPlayer)event.getPlayer()).getHandle().playerConnection;
            final PacketPlayOutCustomPayload packetPlayOutCustomPayload = new PacketPlayOutCustomPayload("REGISTER", new PacketDataSerializer(Unpooled.wrappedBuffer("CB-Client".getBytes())));
            final PacketPlayOutCustomPayload packetPlayOutCustomPayload2 = new PacketPlayOutCustomPayload("REGISTER", new PacketDataSerializer(Unpooled.wrappedBuffer("CC".getBytes())));

            playerConnection.sendPacket(packetPlayOutCustomPayload);
            playerConnection.sendPacket(packetPlayOutCustomPayload2);
        }, 10L);
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        if (this.plugin.getAlertsManager().hasAlertsToggled(event.getPlayer())) {
            this.plugin.getAlertsManager().toggleAlerts(event.getPlayer());
        }
        this.plugin.getPlayerDataManager().removePlayerData(event.getPlayer());
    }
    
    @EventHandler
    public void onTeleport(final PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        final PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
        if (playerData != null) {
            playerData.setSendingVape(true);
        }
    }
    
    @EventHandler
    public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
        if (playerData != null) {
            playerData.setInventoryOpen(false);
        }
    }
    
    @EventHandler
    public void onPlayerAlert(final PlayerAlertEvent event) {
        if (this.plugin.isAntiCheatEnabled()) {
            event.setCancelled(true);
            return;
        }
        final Player player = event.getPlayer();
        if (player == null) {
            return;
        }
        final PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
        if (playerData == null) {
            return;
        }
        final double tps = MinecraftServer.getServer().tps1.getAverage();
        String fixedTPS = new DecimalFormat(".##").format(tps);
        if (tps > 20.0) {
            fixedTPS = "*20.0";
        }

        UtilActionMessage alertMessage = new UtilActionMessage();
        alertMessage.addText(ChatColor.RED.toString() + "[MeMe] " + ChatColor.AQUA + player.getName() + " " + event.getAlert()).addHoverText(ChatColor.YELLOW.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "Click to spectate").setClickEvent(UtilActionMessage.ClickableType.RunCommand, "/spectate " + player.getName());


        for(Player online : Bukkit.getOnlinePlayers()) {

            if(this.plugin.getAlertsManager().hasAlertsToggled(online) && event.getAlertType() == PlayerAlertEvent.AlertType.RELEASE) {
                alertMessage.sendToPlayer(online);
            }

            if(this.plugin.getAlertsManager().hasDevAlertsToggled(online) && (event.getAlertType() == PlayerAlertEvent.AlertType.EXPERIMENTAL || event.getAlertType() == PlayerAlertEvent.AlertType.DEVELOPMENT) ) {
                alertMessage.sendToPlayer(online);
            }
        }

        final String alert = event.getAlert().replace("was caught using a ", "").replace("was caught using ", "") + ChatColor.GRAY + " (Ping " + playerData.getPing() + " ms) (TPS " + fixedTPS + ").";
        final Log log = new Log(player.getUniqueId(), ChatColor.stripColor(alert));
        this.plugin.getLogManager().addToLogQueue(log);
    }
    
    @EventHandler
    public void onPlayerBan(final PlayerBanEvent event) {
        if (this.plugin.isAntiCheatEnabled()) {
            event.setCancelled(true);
            return;
        }
        final Player player = event.getPlayer();
        if (player == null) {
            return;
        }

        this.plugin.getServer().broadcastMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------");
        this.plugin.getServer().broadcastMessage(ChatColor.GOLD + "Lord_MeMe" + ChatColor.RED + " has detected " + ChatColor.GOLD + player.getName() + ChatColor.RED + " cheating.");
        this.plugin.getServer().broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD + "(!) " + ChatColor.RED + "Player has been removed from the network.");
        this.plugin.getServer().broadcastMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------");

        this.plugin.getServer().getScheduler().runTask((Plugin)this.plugin, () -> this.plugin.getServer().dispatchCommand((CommandSender)this.plugin.getServer().getConsoleSender(), "ban -s " + player.getName() + " 30d Unfair Advantage"));

        final PlayerAlertEvent alertEvent = new PlayerAlertEvent(PlayerAlertEvent.AlertType.RELEASE, player, ChatColor.YELLOW + "was banned for " + ChatColor.GOLD + event.getReason() + ".");
        this.plugin.getServer().getPluginManager().callEvent((Event)alertEvent);
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, new ExportLogs(this.plugin));
    }
    
    @EventHandler
    public void onPlayerBanWave(final PlayerBanWaveEvent event) {
        if (this.plugin.isAntiCheatEnabled() && !event.getReason().equalsIgnoreCase("Ban Wave")) {
            return;
        }
        final Player player = event.getPlayer();
        if (player == null) {
            return;
        }


        final PlayerAlertEvent callEvent = new PlayerAlertEvent(PlayerAlertEvent.AlertType.RELEASE, player, ChatColor.YELLOW + "has been added to the next " + ChatColor.GOLD + "Ban Wave" + ChatColor.YELLOW + ".");
        this.plugin.getServer().getPluginManager().callEvent((Event)callEvent);

        final Log log = new Log(player.getUniqueId(), "Added to the next ban wave for " + event.getReason());
        this.plugin.getLogManager().addToLogQueue(log);
    }
    
    public PlayerListener(final LordMeme plugin) {
        this.plugin = plugin;
    }
}
