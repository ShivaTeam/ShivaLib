package net.frozenorb.potpvp.arena.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.arena.ArenaGrid;
import net.frozenorb.potpvp.arena.ArenaHandler;
import net.frozenorb.potpvp.arena.ArenaSchematic;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class ArenaRepasteSchematicCommand {

    @CommandMeta(label = { "arena repasteSchematic" }, permission = "op")
    public static class ArenaRepasteShematic{
        public void execute(Player sender, String schematicName){
            arenaRepasteSchematic(sender, schematicName);
        }
    }

    public static void arenaRepasteSchematic(Player sender, String schematicName) {
        ArenaHandler arenaHandler = PotPvPSI.getInstance().getArenaHandler();
        ArenaSchematic schematic = arenaHandler.getSchematic(schematicName);

        if (schematic == null) {
            sender.sendMessage(ChatColor.RED + "Schematic " + schematicName + " not found.");
            sender.sendMessage(ChatColor.RED + "List all schematics with /arena listSchematics");
            return;
        }


        int currentCopies = arenaHandler.countArenas(schematic);

        if (currentCopies == 0) {
            sender.sendMessage(ChatColor.RED + "No copies of " + schematic.getName() + " exist.");
            return;
        }

        ArenaGrid arenaGrid = arenaHandler.getGrid();

        sender.sendMessage(ChatColor.GREEN + "Starting...");

        arenaGrid.scaleCopies(schematic, 0, () -> {
            sender.sendMessage(ChatColor.GREEN + "Removed old maps, creating new copies...");

            arenaGrid.scaleCopies(schematic, currentCopies, () -> {
                sender.sendMessage(ChatColor.GREEN + "Repasted " + currentCopies + " arenas using the newest " + schematic.getName() + " schematic.");
            });
        });
    }

}