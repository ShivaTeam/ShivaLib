package team.shiva.core.essentials.command;

import team.shiva.core.CoreAPI;
import team.shiva.core.util.BukkitReflection;
import team.shiva.core.util.CC;
import team.shiva.core.util.StyleUtil;
import team.shiva.shivalib.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "ping")
public class PingCommand {

    public void execute(Player player) {
        player.sendMessage(CC.YELLOW + "Your Ping: " + StyleUtil.colorPing(BukkitReflection.getPing(player)));
    }

    public void execute(Player player, Player target) {
        if (target == null) {
            player.sendMessage(CC.RED + "A player with that name could not be found.");
        } else {
            player.sendMessage(CoreAPI.getColoredName(target) + CC.YELLOW + "'s Ping: " +
                               StyleUtil.colorPing(BukkitReflection.getPing(target)));
        }
    }

}
