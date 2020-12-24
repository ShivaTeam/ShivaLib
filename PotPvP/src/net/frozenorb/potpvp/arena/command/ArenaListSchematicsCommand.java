package net.frozenorb.potpvp.arena.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.arena.ArenaHandler;
import net.frozenorb.potpvp.arena.ArenaSchematic;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class ArenaListSchematicsCommand {
    @CommandMeta(label = { "arena listSchematics" } , permission = "op")
    public static class ArenaListSchematics{
        public void execute(Player sender){
            arenaListSchematics(sender);
        }
    }
    public static void arenaListSchematics(Player sender){
        ArenaHandler arenaHandler = PotPvPSI.getInstance().getArenaHandler();
        sender.sendMessage(ChatColor.RED + "------ " + ChatColor.WHITE +"Schematics" + ChatColor.RED + " ------");
        for(ArenaSchematic schematic: arenaHandler.getSchematics()){
            sender.sendMessage(ChatColor.GREEN + schematic.getName());
        }
    }
}
