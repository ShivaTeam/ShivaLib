package net.frozenorb.potpvp.party.command;

import net.frozenorb.potpvp.PotPvPLang;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.party.Party;
import net.frozenorb.potpvp.party.PartyHandler;
import net.frozenorb.potpvp.party.PartyUtils;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class PartyTeamSplitCommand {

    @CommandMeta(label = {"party teamsplit", "p teamsplit", "t teamsplit", "team teamsplit", "f teamsplit"}, permission = "")
    public static class PartyTeamSplit{
        public void execute(Player sender){
            partyTeamSplit(sender);
        }
    }
    public static void partyTeamSplit(Player sender) {
        PartyHandler partyHandler = PotPvPSI.getInstance().getPartyHandler();
        Party party = partyHandler.getParty(sender);

        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
        } else {
            PartyUtils.startTeamSplit(party, sender);
        }
    }

}