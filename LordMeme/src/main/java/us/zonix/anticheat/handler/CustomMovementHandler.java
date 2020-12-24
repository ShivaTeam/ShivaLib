package us.zonix.anticheat.handler;

import com.minexd.spigot.handler.*;
import team.shiva.core.profile.punishment.PunishmentType;
import us.zonix.anticheat.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.commands.ToggleCommand;
import us.zonix.anticheat.util.*;
import us.zonix.anticheat.data.*;
import us.zonix.anticheat.check.*;
import us.zonix.anticheat.util.update.*;
import team.shiva.core.profile.Profile;


public class CustomMovementHandler implements MovementHandler
{
    private final LordMeme plugin;
    
    public void handleUpdateLocation(final Player player, final Location to, final Location from, final PacketPlayInFlying packet) {
        if (player.getAllowFlight()) {
            return;
        }
        if (player.isInsideVehicle()) {
            return;
        }
        if (to.getY() < 2.0) {
            return;
        }
        if (!player.getWorld().isChunkLoaded(to.getBlockX() >> 4, to.getBlockZ() >> 4)) {
            return;
        }
        final PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);

        if (playerData == null) {
            return;
        }

        final Profile profile = Profile.getByUuid(player.getUniqueId());

        if (profile == null) {
            return;
        }

        if(profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
            return;
        }
        
        playerData.setWasOnGround(playerData.isOnGround());
        playerData.setWasInLiquid(playerData.isInLiquid());
        playerData.setWasUnderBlock(playerData.isUnderBlock());
        playerData.setWasInWeb(playerData.isInWeb());
        playerData.setOnGround(BlockUtil.isOnGround(to, 0) || BlockUtil.isOnGround(to, 1));
        if (!playerData.isOnGround()) {
            positions: for (BlockPosition position : playerData.getFakeBlocks()) {
                int x = position.getX();
                int z = position.getZ();

                int blockX = to.getBlock().getX();
                int blockZ = to.getBlock().getZ();

                for (int xOffset = -1; xOffset <= 1; xOffset++) {
                    for (int zOffset = -1; zOffset <= 1; zOffset++) {
                        if (x == blockX + xOffset && z == blockZ + zOffset) {
                            int y = position.getY();
                            int pY = to.getBlock().getY();

                            if (pY - y <= 1 && pY > y) {
                                playerData.setOnGround(true);
                            }

                            if (playerData.isOnGround()) {
                                break positions;
                            }
                        }
                    }
                }
            }
        }
        if (playerData.isOnGround()) {
            playerData.setLastGroundY(to.getY());
        }
        playerData.setInLiquid(BlockUtil.isOnLiquid(to, 0) || BlockUtil.isOnLiquid(to, 1) ||
                               BlockUtil.isOnLiquid(to, -1));
        playerData.setInWeb(BlockUtil.isOnWeb(to, 0));
        playerData.setOnIce(BlockUtil.isOnIce(to, 1) || BlockUtil.isOnIce(to, 2));
        if (playerData.isOnIce()) {
            playerData.setMovementsSinceIce(0);
        }
        else {
            playerData.setMovementsSinceIce(playerData.getMovementsSinceIce() + 1);
        }
        playerData.setOnStairs(BlockUtil.isOnStairs(to, 0) || BlockUtil.isOnStairs(to, 1));
        playerData.setOnCarpet(BlockUtil.isOnCarpet(to, 0) || BlockUtil.isOnCarpet(to, 1));
        playerData.setUnderBlock(BlockUtil.isOnGround(to, -2));
        if (playerData.isUnderBlock()) {
            playerData.setMovementsSinceUnderBlock(0);
        }
        else {
            playerData.setMovementsSinceUnderBlock(playerData.getMovementsSinceUnderBlock() + 1);
        }
        if (to.getY() != from.getY() && playerData.getVelocityV() > 0) {
            playerData.setVelocityV(playerData.getVelocityV() - 1);
        }
        if (Math.hypot(to.getX() - from.getX(), to.getZ() - from.getZ()) > 0.0 && playerData.getVelocityH() > 0) {
            playerData.setVelocityH(playerData.getVelocityH() - 1);
        }
        for (final Class<? extends ICheck> checkClass : PlayerData.CHECKS) {
            if (!ToggleCommand.DISABLED_CHECKS.contains(checkClass.getSimpleName().toUpperCase())) {
                final ICheck check = (ICheck)playerData.getCheck(checkClass);
                if (check != null && check.getType() == PositionUpdate.class) {
                    check.handleCheck(player, new PositionUpdate(player, to, from, packet));
                }
            }
        }
        if (playerData.getVelocityY() > 0.0 && to.getY() > from.getY()) {
            playerData.setVelocityY(0.0);
        }
    }
    
    public void handleUpdateRotation(final Player player, final Location to, final Location from, final PacketPlayInFlying packet) {
        if (player.getAllowFlight()) {
            return;
        }
        final PlayerData playerData = this.plugin.getPlayerDataManager().getPlayerData(player);
        if (playerData == null) {
            return;
        }
        for (final Class<? extends ICheck> checkClass : PlayerData.CHECKS) {
            if (!ToggleCommand.DISABLED_CHECKS.contains(checkClass.getSimpleName().toUpperCase())) {
                final ICheck check = (ICheck)playerData.getCheck(checkClass);
                if (check != null && check.getType() == RotationUpdate.class) {
                    check.handleCheck(player, new RotationUpdate(player, to, from, packet));
                }
            }
        }
    }
    
    public CustomMovementHandler(final LordMeme plugin) {
        this.plugin = plugin;
    }
}
