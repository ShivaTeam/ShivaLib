package team.shiva.shivalib.spawn;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

@CommandMeta(label = "setspawn", permission = "op")
public class SetSpawnCommand {

    public void execute(Player sender) {
        SpawnHandler.setSpawn(sender.getLocation());
        sender.sendMessage(ChatColor.YELLOW + "Spawn point updated!");
    }

}
