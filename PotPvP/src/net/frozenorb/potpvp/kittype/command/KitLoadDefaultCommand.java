package net.frozenorb.potpvp.kittype.command;

import net.frozenorb.potpvp.kittype.KitType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class KitLoadDefaultCommand {

    @CommandMeta(label = "kit loadDefault", permission = "op")
    public static class KitLoadDefault{
        public void execute(Player sender, String kitTypeName){
            KitType kitType = KitType.byId(kitTypeName);
            if(kitType == null){
                sender.sendMessage(ChatColor.RED + "Unknown kit type.");
                return;
            }
            kitLoadDefault(sender, kitType);
        }
    }
    public static void kitLoadDefault(Player sender, KitType kitType) {
        sender.getInventory().setArmorContents(kitType.getDefaultArmor());
        sender.getInventory().setContents(kitType.getDefaultInventory());
        sender.updateInventory();

        sender.sendMessage(ChatColor.YELLOW + "Loaded default armor/inventory for " + kitType + ".");
    }

}