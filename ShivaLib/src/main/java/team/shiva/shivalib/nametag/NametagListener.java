package team.shiva.shivalib.nametag;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import team.shiva.shivalib.ShivaLib;

final class NametagListener
implements Listener {
    NametagListener() {
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (NametagHandler.isInitiated()) {
            event.getPlayer().setMetadata("ShivaLib_nt", new FixedMetadataValue(ShivaLib.getInstance(), true));
            NametagHandler.reloadPlayer(event.getPlayer());
            NametagHandler.reloadOthersFor(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.getPlayer().removeMetadata("ShivaLib_nt", ShivaLib.getInstance());
    }
}

