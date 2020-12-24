package net.frozenorb.potpvp.command;

import net.frozenorb.potpvp.util.VisibilityUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class UpdateVisibilityCommands {

    @CommandMeta(label = {"updatevisibility", "updatevis", "upvis", "uv"}, permission = "")
    public static class UpdateVisibility{
        public void execute(Player sender){
            updateVisibility(sender);
        }
    }
    public static void updateVisibility(Player sender) {
        VisibilityUtils.updateVisibility(sender);
        sender.sendMessage(ChatColor.GREEN + "Updated your visibility.");
    }

    @CommandMeta(label = {"updatevisibilityFlicker", "updatevisFlicker", "upvisFlicker", "uvf"}, permission = "")
    public static class UpdateVisibilityFlicker{
        public void execute(Player sender){
            updateVisibilityFlicker(sender);
        }
    }

    public static void updateVisibilityFlicker(Player sender) {
        VisibilityUtils.updateVisibilityFlicker(sender);
        sender.sendMessage(ChatColor.GREEN + "Updated your visibility (flicker mode).");
    }

}