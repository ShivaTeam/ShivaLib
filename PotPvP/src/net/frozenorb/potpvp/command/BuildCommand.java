package net.frozenorb.potpvp.command;

import net.frozenorb.potpvp.PotPvPSI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class BuildCommand {

    @CommandMeta(label = {"build"}, permission = "op")
    public static class Silent{
        public void execute(Player sender){
            silent(sender);
        }
    }
    public static void silent(Player sender) {
        if (sender.hasMetadata("Build")) {
            sender.removeMetadata("Build", PotPvPSI.getInstance());
            sender.sendMessage(ChatColor.RED + "Build mode disabled.");
        } else {
            sender.setMetadata("Build", new FixedMetadataValue(PotPvPSI.getInstance(), true));
            sender.sendMessage(ChatColor.GREEN + "Build mode enabled.");
        }
    }

}