package net.frozenorb.potpvp.party.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.party.PartyHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class PartyCreateCommand {

    @CommandMeta(label = {"party create", "p create", "t create", "team create", "f create"}, permission = "")
    public static class PartyCreate{
        public void execute(Player sender){
            partyCreate(sender);
        }
    }
    public static void partyCreate(Player sender) {
        PartyHandler partyHandler = PotPvPSI.getInstance().getPartyHandler();

        if (partyHandler.hasParty(sender)) {
            sender.sendMessage(ChatColor.RED + "You are already in a party.");
            return;
        }

        partyHandler.getOrCreateParty(sender);
        sender.sendMessage(ChatColor.YELLOW + "Created a new party.");
    }

}