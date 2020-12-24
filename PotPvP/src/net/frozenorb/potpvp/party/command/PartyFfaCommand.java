package net.frozenorb.potpvp.party.command;

import net.frozenorb.potpvp.PotPvPLang;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.kittype.menu.select.SelectKitTypeMenu;
import net.frozenorb.potpvp.match.Match;
import net.frozenorb.potpvp.match.MatchHandler;
import net.frozenorb.potpvp.match.MatchTeam;
import net.frozenorb.potpvp.party.Party;
import net.frozenorb.potpvp.party.PartyHandler;
import net.frozenorb.potpvp.validation.PotPvPValidation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class PartyFfaCommand {

    @CommandMeta(label = {"party ffa", "p ffa", "t ffa", "team ffa", "f ffa"}, permission = "")
    public static class PartyFfa{
        public void execute(Player sender){
            partyFfa(sender);
        }
    }
    public static void partyFfa(Player sender) {
        PartyHandler partyHandler = PotPvPSI.getInstance().getPartyHandler();
        Party party = partyHandler.getParty(sender);

        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
        } else {
            MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();

            if (!PotPvPValidation.canStartFfa(party, sender)) {
                return;
            }

            new SelectKitTypeMenu(kitType -> {
                sender.closeInventory();

                if (!PotPvPValidation.canStartFfa(party, sender)) {
                    return;
                }

                List<MatchTeam> teams = new ArrayList<>();

                for (UUID member : party.getMembers()) {
                    teams.add(new MatchTeam(member));
                }

                matchHandler.startMatch(teams, kitType, false, false);
            }, "Start a Party FFA...").openMenu(sender);
        }
    }

    @CommandMeta(label = {"party devffa", "p devffa", "t devffa", "team devffa", "f devffa"}, permission = "")
    public static class PartyDevFfa{
        public void execute(Player sender, int teamSize){
            partyDevFfa(sender, teamSize);
        }
        public void execute(Player sender){
            partyDevFfa(sender, 1);
        }
    }
    public static void partyDevFfa(Player sender, int teamSize) {
        PartyHandler partyHandler = PotPvPSI.getInstance().getPartyHandler();
        Party party = partyHandler.getParty(sender);

        if (party == null) {
            sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
        } else if (!party.isLeader(sender.getUniqueId())) {
            sender.sendMessage(PotPvPLang.NOT_LEADER_OF_PARTY);
        } else {
            MatchHandler matchHandler = PotPvPSI.getInstance().getMatchHandler();

            if (!PotPvPValidation.canStartFfa(party, sender)) {
                return;
            }

            new SelectKitTypeMenu(kitType -> {
                sender.closeInventory();

                if (!PotPvPValidation.canStartFfa(party, sender)) {
                    return;
                }

                List<UUID> availableMembers = new ArrayList<>(party.getMembers());
                Collections.shuffle(availableMembers);
                
                List<MatchTeam> teams = new ArrayList<>();

                while (availableMembers.size() >= teamSize) {
                    List<UUID> teamMembers = new ArrayList<>();

                    for (int i = 0; i < teamSize; i++) {
                        teamMembers.add(availableMembers.remove(0));
                    }

                    teams.add(new MatchTeam(teamMembers));
                }

                Match match = matchHandler.startMatch(teams, kitType, false, false);

                if (match != null) {
                    for (UUID leftOut : availableMembers) {
                        match.addSpectator(Bukkit.getPlayer(leftOut), null);
                    }
                }
            }, "Start Dev Party FFA...").openMenu(sender);
        }
    }

}