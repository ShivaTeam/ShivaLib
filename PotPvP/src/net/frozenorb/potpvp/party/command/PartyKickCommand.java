package net.frozenorb.potpvp.party.command;

import net.frozenorb.potpvp.PotPvPLang;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.party.Party;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class PartyKickCommand {

    @CommandMeta(label = {"party kick", "p kick", "t kick", "team kick", "f kick"}, permission = "")
    public static class PlayerKick{
        public void execute(Player sender, Player player){
            partyKick(sender, player);
        }
    }
    public static void partyKick(Player sender, Player target) {
        if(target == null){
            sender.sendMessage(ChatColor.RED + "A player with that name could not be found.");
            return;
        }
        Party party = PotPvPSI.getInstance().getPartyHandler().getParty(sender);

        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
        } else if (sender == target) {
            sender.sendMessage(ChatColor.RED + "You cannot kick yourself.");
        } else if (!party.isMember(target.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + target.getName() + " isn't in your party.");
        } else {
            party.kick(target);
        }
    }

}