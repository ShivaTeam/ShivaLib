package net.frozenorb.potpvp.party.command;

import net.frozenorb.potpvp.PotPvPLang;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.party.Party;
import net.frozenorb.potpvp.party.PartyAccessRestriction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class PartyLockCommand {

    @CommandMeta(label = {"party lock", "p lock", "t lock", "team lock", "f lock"}, permission = "")
    public static class PartyLock{
        public void execute(Player sender){
            partyLock(sender);
        }
    }
    public static void partyLock(Player sender) {
        Party party = PotPvPSI.getInstance().getPartyHandler().getParty(sender);

        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
        } else if (party.getAccessRestriction() == PartyAccessRestriction.INVITE_ONLY) {
            sender.sendMessage(ChatColor.RED + "Your party is already locked.");
        } else {
            party.setAccessRestriction(PartyAccessRestriction.INVITE_ONLY);
            sender.sendMessage(ChatColor.YELLOW + "Your party is now " + ChatColor.RED + "locked" + ChatColor.YELLOW + ".");
        }
    }

}
