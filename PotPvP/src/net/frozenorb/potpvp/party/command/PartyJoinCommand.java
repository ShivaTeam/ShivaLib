package net.frozenorb.potpvp.party.command;

import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.party.Party;
import net.frozenorb.potpvp.party.PartyHandler;
import net.frozenorb.potpvp.party.PartyInvite;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;

public final class PartyJoinCommand {

    // default value for password parameter used to detect that password
    // wasn't provided. No Optional<String> :(
    private static final String NO_PASSWORD_PROVIDED = "skasjkdasdjhksahjd";

    @CommandMeta(label = {"party join", "p join", "t join", "team join", "f join"}, permission = "")
    public static class PartyJoin{
        public void execute(Player sender, Player player, String providedPassword){
            partyJoin(sender, player, providedPassword);
        }
        public void execute(Player sender, Player player){
            partyJoin(sender, player, NO_PASSWORD_PROVIDED);
        }
    }
    public static void partyJoin(Player sender, Player target, String providedPassword) {
        if(target == null){
            sender.sendMessage(ChatColor.RED + "A player with that name could not be found.");
            return;
        }
        PartyHandler partyHandler = PotPvPSI.getInstance().getPartyHandler();
        Party targetParty = partyHandler.getParty(target);

        if (partyHandler.hasParty(sender)) {
            sender.sendMessage(ChatColor.RED + "You are already in a party. You must leave your current party first.");
            return;
        }

        if (targetParty == null) {
            sender.sendMessage(ChatColor.RED + target.getName() + " is not in a party.");
            return;
        }

        PartyInvite invite = targetParty.getInvite(sender.getUniqueId());

        switch (targetParty.getAccessRestriction()) {
            case PUBLIC:
                targetParty.join(sender);
                break;
            case INVITE_ONLY:
                if (invite != null) {
                    targetParty.join(sender);
                } else {
                    sender.sendMessage(ChatColor.RED + "You don't have an invitation to this party.");
                }

                break;
            case PASSWORD:
                if (providedPassword.equals(NO_PASSWORD_PROVIDED) && invite == null) {
                    sender.sendMessage(ChatColor.RED + "You need the password or an invitation to join this party.");
                    sender.sendMessage(ChatColor.GRAY + "To join with a password, use " + ChatColor.YELLOW + "/party join " + target.getName() + " <password>");
                    return;
                }

                String correctPassword = targetParty.getPassword();

                if (invite == null && !correctPassword.equals(providedPassword)) {
                    sender.sendMessage(ChatColor.RED + "Invalid password.");
                } else {
                    targetParty.join(sender);
                }

                break;
            default:
                break;
        }
    }

}