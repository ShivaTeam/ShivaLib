package net.frozenorb.potpvp.morpheus;

import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameQueue;
import com.qrakn.morpheus.game.GameState;
import com.qrakn.morpheus.game.bukkit.event.GameStateChangeEvent;
import com.qrakn.morpheus.game.bukkit.event.PlayerGameInteractionEvent;
import com.qrakn.morpheus.game.bukkit.event.PlayerJoinGameEvent;
import com.qrakn.morpheus.game.bukkit.event.PlayerQuitGameEvent;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.morpheus.menu.EventsMenu;
import net.frozenorb.potpvp.util.VisibilityUtils;
import team.shiva.shivalib.nametag.NametagHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventListeners implements Listener {

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() != null && event.getItem().equals(EventItems.getEventItem()) && PotPvPSI.getInstance().getLobbyHandler().isInLobby(player)) {
            /*if (GameQueue.INSTANCE.getCurrentGames().size() == 1) {
                Game game = GameQueue.INSTANCE.getCurrentGames().get(0);
                if (game.getState() == GameState.STARTING) {
                    if (game.getMaxPlayers() > 0 && game.getPlayers().size() >= game.getMaxPlayers()) {
                        player.sendMessage(ChatColor.RED + "This event is currently full! Sorry!");
                        return;
                    }
                    game.add(player);
                } else {
                    game.addSpectator(player);
                }
                return;
            }*/
            new EventsMenu().openMenu(player);
        }

    }

    @EventHandler
    public void onGameStateChangeEvent(GameStateChangeEvent event) {
        Game game = event.getGame();

        if (event.getTo() == GameState.ENDED) {
            PotPvPSI.getInstance().getArenaHandler().releaseArena(game.getArena());
            for (Player player : game.getPlayers()) {
                NametagHandler.reloadPlayer(player);
                NametagHandler.reloadOthersFor(player);
                VisibilityUtils.updateVisibility(player);
                PotPvPSI.getInstance().getLobbyHandler().returnToLobby(player);
            }
        }
    }

    @EventHandler
    public void onPlayerJoinGameEvent(PlayerJoinGameEvent event) {
        NametagHandler.reloadPlayer(event.getPlayer());
        NametagHandler.reloadOthersFor(event.getPlayer());
        for (Player player : event.getGame().getPlayers()) {
            VisibilityUtils.updateVisibility(player);
        }
    }

    @EventHandler
    public void onPlayerQuitGameEvent(PlayerQuitGameEvent event) {
        NametagHandler.reloadPlayer(event.getPlayer());
        NametagHandler.reloadOthersFor(event.getPlayer());
        PotPvPSI.getInstance().getLobbyHandler().returnToLobby(event.getPlayer());
    }

    @EventHandler
    public void onPlayerGameInteractionEvent(PlayerGameInteractionEvent event) {
        NametagHandler.reloadPlayer(event.getPlayer());
        NametagHandler.reloadOthersFor(event.getPlayer());
        VisibilityUtils.updateVisibility(event.getPlayer());
    }

}
