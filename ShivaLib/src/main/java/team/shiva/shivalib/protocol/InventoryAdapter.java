package team.shiva.shivalib.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import team.shiva.shivalib.ShivaLib;
import team.shiva.shivalib.protocol.event.PlayerCloseInventoryEvent;
import team.shiva.shivalib.protocol.event.PlayerOpenInventoryEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InventoryAdapter extends PacketAdapter {
    private static final Set<UUID> currentlyOpen = new HashSet<>();

    public InventoryAdapter() {
        super(ShivaLib.getInstance(), PacketType.Play.Client.CLIENT_COMMAND, PacketType.Play.Client.CLOSE_WINDOW);
    }

    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PacketContainer packet = event.getPacket();
        if (packet.getType() == PacketType.Play.Client.CLIENT_COMMAND && packet.getClientCommands().size() != 0 && packet.getClientCommands().read(0) == EnumWrappers.ClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
            if (!currentlyOpen.contains(player.getUniqueId())) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(ShivaLib.getInstance(), () -> Bukkit.getPluginManager().callEvent(new PlayerOpenInventoryEvent(player)));
            }
            currentlyOpen.add(player.getUniqueId());
        } else if (packet.getType() == PacketType.Play.Client.CLOSE_WINDOW) {
            if (currentlyOpen.contains(player.getUniqueId())) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(ShivaLib.getInstance(), () -> Bukkit.getPluginManager().callEvent(new PlayerCloseInventoryEvent(player)));
            }
            currentlyOpen.remove(player.getUniqueId());
        }
    }

    public static Set<UUID> getCurrentlyOpen() {
        return currentlyOpen;
    }
}

