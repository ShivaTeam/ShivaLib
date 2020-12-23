package team.shiva.shivalib.util;

import org.bukkit.entity.Player;
import team.shiva.shivalib.ShivaLib;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class BungeeUtils {
    private BungeeUtils() {
    }

    public static void send(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        }
        catch (IOException iOException) {
            // empty catch block
        }
        player.sendPluginMessage(ShivaLib.getInstance(), "BungeeCord", b.toByteArray());
    }

    public static void sendAll(String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        }
        catch (IOException iOException) {
            // empty catch block
        }
        for (Player player : ShivaLib.getInstance().getServer().getOnlinePlayers()) {
            player.sendPluginMessage(ShivaLib.getInstance(), "BungeeCord", b.toByteArray());
        }
    }
}

