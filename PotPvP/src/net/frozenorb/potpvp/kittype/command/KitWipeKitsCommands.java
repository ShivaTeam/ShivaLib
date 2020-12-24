package net.frozenorb.potpvp.kittype.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.kittype.KitType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;
import team.shiva.shivalib.util.UUIDUtils;

import java.util.UUID;

public final class KitWipeKitsCommands {

    @CommandMeta(label = "kit wipeKits Type", permission = "op")
    public static class KitWipeKitsType{
        public void execute(Player sender, String kitTypeName){
            KitType kitType = KitType.byId(kitTypeName);
            if(kitType == null){
                sender.sendMessage(ChatColor.RED + "Unknown kit type.");
                return;
            }
            kitWipeKitsType(sender, kitType);
        }
    }
    public static void kitWipeKitsType(Player sender, KitType kitType) {
        int modified = PotPvPSI.getInstance().getKitHandler().wipeKitsWithType(kitType);
        sender.sendMessage(ChatColor.YELLOW + "Wiped " + modified + " " + kitType.getDisplayName() + " kits.");
        sender.sendMessage(ChatColor.GRAY + "^ We would have a proper count here if we ran recent versions of MongoDB");
    }

    @CommandMeta(label = "kit wipeKits Player", permission = "op")
    public static class KitWipeKitsPlayer{
        public void execute(Player sender, String player){
            UUID target = UUIDUtils.uuid(player);
            if(target == null){
                sender.sendMessage(ChatColor.RED + "Unknown player.");
                return;
            }
            kitWipeKitsPlayer(sender, target);
        }
    }
    public static void kitWipeKitsPlayer(Player sender, UUID target) {
        PotPvPSI.getInstance().getKitHandler().wipeKitsForPlayer(target);
        sender.sendMessage(ChatColor.YELLOW + "Wiped " + UUIDUtils.name(target) + "'s kits.");
    }

}