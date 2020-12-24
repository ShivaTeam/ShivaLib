package net.frozenorb.potpvp.postmatchinv.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.postmatchinv.PostMatchInvHandler;
import net.frozenorb.potpvp.postmatchinv.PostMatchPlayer;
import net.frozenorb.potpvp.postmatchinv.menu.PostMatchMenu;
import team.shiva.shivalib.util.UUIDUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

import java.util.Map;
import java.util.UUID;

public final class CheckPostMatchInvCommand {

    @CommandMeta(label = { "checkPostMatchInv", "_" }, permission = "")
    public static class CheckPostMatchInv{
        public void execute(Player sender, String target){
            checkPostMatchInv(sender, UUIDUtils.uuid(target));
        }
    }
    public static void checkPostMatchInv(Player sender, UUID target) {
        PostMatchInvHandler postMatchInvHandler = PotPvPSI.getInstance().getPostMatchInvHandler();
        Map<UUID, PostMatchPlayer> players = postMatchInvHandler.getPostMatchData(sender.getUniqueId());

        if (players.containsKey(target)) {
            new PostMatchMenu(players.get(target)).openMenu(sender);
        } else {
            sender.sendMessage(ChatColor.RED + "Data for " + UUIDUtils.name(target) + " not found.");
        }
    }

}