package net.frozenorb.potpvp.elo.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.elo.EloHandler;
import net.frozenorb.potpvp.kittype.KitType;
import net.frozenorb.potpvp.party.Party;
import net.frozenorb.potpvp.party.PartyHandler;
import team.shiva.shivalib.honcho.command.CommandMeta;
import team.shiva.shivalib.util.UUIDUtils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class EloSetCommands {

    @CommandMeta(label = {"elo setSolo"}, permission = "op")
    public static class EloSetSolo{
        public void execute(Player sender, Player target, String kitTypeName, int newElo){
            if(target == null){
                sender.sendMessage(ChatColor.RED + "A player with that name could not be found.");
                return;
            }
            KitType kitType = KitType.byId(kitTypeName);
            if(kitType == null){
                sender.sendMessage(ChatColor.RED + "Unknown kit type.");
                return;
            }
            eloSetSolo(sender, target, kitType, newElo);
        }
    }
    public static void eloSetSolo(Player sender,  Player target,  KitType kitType,  int newElo) {
        EloHandler eloHandler = PotPvPSI.getInstance().getEloHandler();
        eloHandler.setElo(target, kitType, newElo);
        sender.sendMessage(ChatColor.YELLOW + "Set " + target.getName() + "'s " + kitType.getDisplayName() + " elo to " + newElo + ".");
    }

    @CommandMeta(label = {"elo setTeam"}, permission = "op")
    public static class EloSetTeam{
        public void execute(Player sender, Player target, String kitTypeName, int newElo){
            KitType kitType = KitType.byId(kitTypeName);
            if(kitType == null){
                sender.sendMessage(ChatColor.RED + "Unknown kit type.");
                return;
            }
            eloSetTeam(sender, target, kitType, newElo);
        }
    }
    public static void eloSetTeam(Player sender, Player target, KitType kitType, int newElo) {
        PartyHandler partyHandler = PotPvPSI.getInstance().getPartyHandler();
        EloHandler eloHandler = PotPvPSI.getInstance().getEloHandler();

        Party targetParty = partyHandler.getParty(target);

        if (targetParty == null) {
            sender.sendMessage(ChatColor.RED + target.getName() + " is not in a party.");
            return;
        }

        eloHandler.setElo(targetParty.getMembers(), kitType, newElo);
        sender.sendMessage(ChatColor.YELLOW + "Set " + kitType.getDisplayName() + " elo of " + UUIDUtils.name(targetParty.getLeader()) + "'s party to " + newElo + ".");
    }

}