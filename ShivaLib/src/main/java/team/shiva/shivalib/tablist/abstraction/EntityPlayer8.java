package team.shiva.shivalib.tablist.abstraction;

import team.shiva.shivalib.tablist.util.TabIcon;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.*;
import org.bukkit.*;
import net.minecraft.server.v1_8_R3.*;
import java.util.*;
import com.mojang.authlib.*;

public class EntityPlayer8 extends EntityPlayerWrapper
{
    private EntityPlayer entityPlayer;
    private MinecraftServer minecraftServer;
    private WorldServer worldServer;
    private PlayerInteractManager playerInteractManager;
    private int ping;

    @Override
    public String getName() {
        return this.entityPlayer.getName();
    }

    public EntityPlayer8(final String rowString, final TabIcon tabIcon, int ping) {
        this.minecraftServer = ((CraftServer)Bukkit.getServer()).getServer();
        this.worldServer = this.minecraftServer.getWorldServer(0);
        this.playerInteractManager = new PlayerInteractManager(this.worldServer);
        final GameProfile gameProfile = new GameProfile(UUID.randomUUID(), rowString);
        if (tabIcon != null) {
            gameProfile.getProperties().put("textures", tabIcon.getProperty());
        }
        final EntityPlayer entityPlayer = new EntityPlayer(this.minecraftServer, this.worldServer, gameProfile, this.playerInteractManager);
        this.entityPlayer = entityPlayer;
        this.entityPlayer.ping = ping;
    }

    public EntityPlayer getEntityPlayer() {
        return this.entityPlayer;
    }
}
