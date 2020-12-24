package net.frozenorb.potpvp.party.command;

import net.frozenorb.potpvp.PotPvPLang;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.party.Party;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class PartyDisbandCommand {

    @CommandMeta(label = {"party disband", "p disband", "t disband", "team disband", "f disband"}, permission = "")
    public static class PartyDisband{
        public void execute(Player sender){
            partyDisband(sender);
        }
    }
    public static void partyDisband(Player sender) {
        Party party = PotPvPSI.getInstance().getPartyHandler().getParty(sender);

        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
            return;
        }

        if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
            return;
        }

        party.disband();
    }

}