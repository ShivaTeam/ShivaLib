package net.frozenorb.potpvp.party.command;

import net.frozenorb.potpvp.PotPvPLang;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.party.Party;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class PartyLeaderCommand {

    @CommandMeta(label = {"party leader", "p leader", "t leader", "team leader", "leader", "f leader"}, permission = "")
    public static class PartyLeader{
        public void execute(Player sender, Player player){
            partyLeader(sender, player);
        }
    }
    public static void partyLeader(Player sender, Player target) {
        if(target == null){
            sender.sendMessage(ChatColor.RED + "A player with that name could not be found.");
            return;
        }
        Party party = PotPvPSI.getInstance().getPartyHandler().getParty(sender);

        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
        } else if (!party.isMember(target.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + target.getName() + " isn't in your party.");
        } else if (sender == target) {
            sender.sendMessage(ChatColor.RED + "You cannot promote yourself to the leader of your own party.");
        } else {
            party.setLeader(target);
        }
    }

}