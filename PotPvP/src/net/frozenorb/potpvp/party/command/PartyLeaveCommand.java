package net.frozenorb.potpvp.party.command;

import net.frozenorb.potpvp.PotPvPLang;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.party.Party;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class PartyLeaveCommand {

    @CommandMeta(label = {"party leave", "p leave", "t leave", "team leave", "leave", "f leave"}, permission = "")
    public static class PartyLeave{
        public void execute(Player sender){
            partyLeave(sender);
        }
    }
    public static void partyLeave(Player sender) {
        Party party = PotPvPSI.getInstance().getPartyHandler().getParty(sender);

        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else {
            party.leave(sender);
        }
    }

}