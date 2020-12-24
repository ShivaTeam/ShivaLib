package net.frozenorb.potpvp.arena.listener;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.arena.Arena;
import net.frozenorb.potpvp.arena.ArenaHandler;
import net.frozenorb.potpvp.arena.ArenaSchematic;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class ArenaBlockChangeListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ArenaHandler arenaHandler = PotPvPSI.getInstance().getArenaHandler();
        for(ArenaSchematic schematic: arenaHandler.getSchematics()){
            for(Arena arena: arenaHandler.getArenas(schematic)){
                if(arena.getBounds().contains(event.getBlock())){
                    arena.recordBlockChange(event.getBlock());
                    break;
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        ArenaHandler arenaHandler = PotPvPSI.getInstance().getArenaHandler();
        for(ArenaSchematic schematic: arenaHandler.getSchematics()){
            for(Arena arena: arenaHandler.getArenas(schematic)){
                if(arena.getBounds().contains(event.getBlock())){
                    arena.recordBlockChange(event.getBlock());
                    break;
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockForm(BlockFormEvent event){
        ArenaHandler arenaHandler = PotPvPSI.getInstance().getArenaHandler();
        for(ArenaSchematic schematic: arenaHandler.getSchematics()){
            for(Arena arena: arenaHandler.getArenas(schematic)){
                if(arena.getBounds().contains(event.getBlock())){
                    arena.recordBlockChange(event.getBlock());
                    break;
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event){
        ArenaHandler arenaHandler = PotPvPSI.getInstance().getArenaHandler();
        for(ArenaSchematic schematic: arenaHandler.getSchematics()){
            for(Arena arena: arenaHandler.getArenas(schematic)){
                if(arena.getBounds().contains(event.getToBlock())){
                    arena.recordBlockChange(event.getToBlock());
                    break;
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        ArenaHandler arenaHandler = PotPvPSI.getInstance().getArenaHandler();
        Block block = event.getBlockClicked().getRelative(event.getBlockFace());
        for(ArenaSchematic schematic: arenaHandler.getSchematics()){
            for(Arena arena: arenaHandler.getArenas(schematic)){
                if(arena.getBounds().contains(block)){
                    arena.recordBlockChange(block);
                    break;
                }
            }
        }
    }
}