package team.shiva.shivalib.protocol.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerOpenInventoryEvent
extends PlayerEvent {
    private static HandlerList handlerList = new HandlerList();

    public PlayerOpenInventoryEvent(Player player) {
        super(player);
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}

