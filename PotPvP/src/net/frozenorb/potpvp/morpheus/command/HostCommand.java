package net.frozenorb.potpvp.morpheus.command;

import net.frozenorb.potpvp.morpheus.menu.HostMenu;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public class HostCommand {

    @CommandMeta(label = { "host"}, permission = "")
    public static class Host{
        public void execute(Player sender){
            host(sender);
        }
    }
    public static void host(Player sender) {
        new HostMenu().openMenu(sender);
    }

}
