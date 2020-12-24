package us.zonix.anticheat.manager;

import org.bukkit.Bukkit;
import us.zonix.anticheat.*;
import org.bukkit.entity.*;

import java.util.*;

public class AlertsManager
{
    private final Set<UUID> devAlertsToggled;
    private final Set<UUID> alertsToggled;
    private final LordMeme plugin;
    
    public boolean hasAlertsToggled(final Player player) {
        return this.alertsToggled.contains(player.getUniqueId());
    }

    public boolean hasDevAlertsToggled(final Player player) {
        return this.devAlertsToggled.contains(player.getUniqueId());
    }


    public void toggleAlerts(final Player player) {
        if (!this.alertsToggled.remove(player.getUniqueId())) {
            this.alertsToggled.add(player.getUniqueId());
        }
    }

    public void toggleDevAlerts(final Player player) {
        if (!this.devAlertsToggled.remove(player.getUniqueId())) {
            this.devAlertsToggled.add(player.getUniqueId());
        }
    }
    
    public void forceAlert(final String message) {

        for (UUID uuid : this.alertsToggled) {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    public void forceDevAlert(final String message) {

        for (UUID uuid : this.devAlertsToggled) {
            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                player.sendMessage(message);
            }
        }
    }
    
    public AlertsManager(final LordMeme plugin) {
        this.alertsToggled = new HashSet<UUID>();
        this.devAlertsToggled = new HashSet<>();
        this.plugin = plugin;
    }
    
    public Set<UUID> getAlertsToggled() {
        return this.alertsToggled;
    }

    public Set<UUID> getDevAlertsToggled() {
        return devAlertsToggled;
    }
}
