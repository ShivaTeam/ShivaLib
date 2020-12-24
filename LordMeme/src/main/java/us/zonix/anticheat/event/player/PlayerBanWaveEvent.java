package us.zonix.anticheat.event.player;

import org.bukkit.event.*;
import org.bukkit.entity.*;

public class PlayerBanWaveEvent extends Event implements Cancellable
{
    private static final HandlerList HANDLER_LIST;
    private final Player player;
    private final String reason;
    private boolean cancelled;
    
    public static HandlerList getHandlerList() {
        return PlayerBanWaveEvent.HANDLER_LIST;
    }
    
    public HandlerList getHandlers() {
        return PlayerBanWaveEvent.HANDLER_LIST;
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public String getReason() {
        return this.reason;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public PlayerBanWaveEvent(final Player player, final String reason) {
        this.player = player;
        this.reason = reason;
    }
    
    static {
        HANDLER_LIST = new HandlerList();
    }
}
