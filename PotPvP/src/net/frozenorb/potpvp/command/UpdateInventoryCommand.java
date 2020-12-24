package net.frozenorb.potpvp.command;

import net.frozenorb.potpvp.util.InventoryUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

/**
 * /updateInventory command, typically only used for debugging inventory
 * issues. Available to all players to enforce the constraint that
 * {@link InventoryUtils#resetInventoryDelayed(Player)}
 * can always be called at any time.
 */
public final class UpdateInventoryCommand {

    @CommandMeta(label = {"updateinventory", "updateinv", "upinv", "ui"}, permission = "")
    public static class UpdateInventory{
        public void execute(Player sender){
            updateInventory(sender);
        }
    }
    public static void updateInventory(Player sender) {
        InventoryUtils.resetInventoryDelayed(sender);
        sender.sendMessage(ChatColor.GREEN + "Updated your inventory.");
    }

}