package net.frozenorb.potpvp.arena.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.arena.Arena;
import net.frozenorb.potpvp.arena.ArenaHandler;
import net.frozenorb.potpvp.arena.ArenaSchematic;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class ArenaScaleCommand {

    @CommandMeta(label= { "arena scale" }, permission = "op")
    public static class ArenaScale{
        public void execute(Player sender, String schematicName, int count){
            arenaScale(sender, schematicName, count);
        }
    }
    public static void arenaScale(Player sender, String schematicName, int count) {
        ArenaHandler arenaHandler = PotPvPSI.getInstance().getArenaHandler();
        ArenaSchematic schematic = arenaHandler.getSchematic(schematicName);

        if (schematic == null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " not found.");
            sender.sendMessage(ChatColor.RED + "List all schematics with /arena listSchematics");
            return;
        }

        sender.sendMessage(ChatColor.GREEN + "Starting...");

        arenaHandler.getGrid().scaleCopies(schematic, count, () -> {
            sender.sendMessage(ChatColor.GREEN + "Scaled " + schematic.getName() + " to " + count + " copies.");
        });
    }

    @CommandMeta(label = "arena rescaleall", permission = "op")
    public static class ArenaRescaleAll{
        public void execute(Player sender){
            arenaRescaleAll(sender);
        }
    }

    public static void arenaRescaleAll(Player sender) {
        PotPvPSI.getInstance().getArenaHandler().getSchematics().forEach(schematic -> {
            ArenaHandler arenaHandler = PotPvPSI.getInstance().getArenaHandler();
            int totalCopies = 0;
            int inUseCopies = 0;

            for (Arena arena : arenaHandler.getArenas(schematic)) {
                totalCopies++;
            }

            arenaScale(sender, schematic.getName(), 0);
            arenaScale(sender, schematic.getName(), totalCopies);
        });
    }

}