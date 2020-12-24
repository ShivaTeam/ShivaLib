package net.frozenorb.potpvp.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class KillCommands {

    @CommandMeta(label = {"suicide"}, permission = "")
    public static class Suicide{
        public void execute(Player sender){
            suicide(sender);
        }
    }
    public static void suicide(Player sender) {
        sender.sendMessage(ChatColor.RED + "/suicide has been disabled.");
    }

    @CommandMeta(label = {"kill"}, permission = "basic.kill")
    public static class Kill{
        public void execute(Player sender, Player target){
            kill(sender, target);
        }
    }
    public static void kill(Player sender, Player target) {
        if(target == null){
            sender.sendMessage(ChatColor.RED + "A player with that name could not be found.");
            return;
        }
        target.setHealth(0);
        sender.sendMessage(target.getDisplayName() + ChatColor.GOLD + " has been killed.");
    }

}