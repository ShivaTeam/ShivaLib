package net.frozenorb.potpvp.arena.command;

import net.frozenorb.potpvp.PotPvPSI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class ArenaFreeCommand {

    @CommandMeta(label = { "arena free" }, permission = "op")
    public static class ArenaFree{
        public void execute(Player sender){
            arenaFree(sender);
        }
    }
    public static void arenaFree(Player sender) {
        PotPvPSI.getInstance().getArenaHandler().getGrid().free();
        sender.sendMessage(ChatColor.GREEN + "Arena grid has been freed.");
    }

}