package net.frozenorb.potpvp.kittype.command;

import net.frozenorb.potpvp.kittype.KitType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class KitSaveDefaultCommand {

    @CommandMeta(label = "kit saveDefault", permission = "op")
    public static class KitSaveDefault{
        public void execute(Player sender, String kitTypeName){
            KitType kitType = KitType.byId(kitTypeName);
            if(kitType == null){
                sender.sendMessage(ChatColor.RED + "Unknown kit type.");
                return;
            }
            kitSaveDefault(sender, kitType);
        }
    }
    public static void kitSaveDefault(Player sender, KitType kitType) {
        kitType.setDefaultArmor(sender.getInventory().getArmorContents());
        kitType.setDefaultInventory(sender.getInventory().getContents());
        kitType.saveAsync();

        sender.sendMessage(ChatColor.YELLOW + "Saved default armor/inventory for " + kitType + ".");
    }

}