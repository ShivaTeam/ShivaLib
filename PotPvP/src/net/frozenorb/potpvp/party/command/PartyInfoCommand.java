package net.frozenorb.potpvp.party.command;

import com.google.common.base.Joiner;
import net.frozenorb.potpvp.PotPvPLang;
import net.frozenorb.potpvp.PotPvPSI;
import net.frozenorb.potpvp.party.Party;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;
import team.shiva.shivalib.util.PlayerUtils;
import team.shiva.shivalib.util.UUIDUtils;

public final class PartyInfoCommand {

    @CommandMeta(label = {"party info", "p info", "t info", "team info", "f info", "p i", "t i", "f i", "party i", "team i"}, permission = "")
    public static class PartyInfo{
        public void execute(Player sender, Player player){
            partyInfo(sender, player);
        }
        public void execute(Player sender){
            partyInfo(sender, sender);
        }
    }
    public static void partyInfo(Player sender, Player target) {
        if(target == null){
            sender.sendMessage(org.bukkit.ChatColor.RED + "A player with that name could not be found.");
            return;
        }
        Party party = PotPvPSI.getInstance().getPartyHandler().getParty(target);

        if (party == null) {
            if (sender == target) {
                sender.sendMessage(PotPvPLang.NOT_IN_PARTY);
            } else {
                sender.sendMessage(ChatColor.RED + target.getName() + " isn't in a party.");
            }

            return;
        }

        String leaderName = UUIDUtils.name(party.getLeader());
        int memberCount = party.getMembers().size();
        String members = Joiner.on(", ").join(PlayerUtils.mapToNames(party.getMembers()));

        sender.sendMessage(ChatColor.GRAY + PotPvPLang.LONG_LINE);
        sender.sendMessage(ChatColor.YELLOW + "Leader: " + ChatColor.GOLD + leaderName);
        sender.sendMessage(ChatColor.YELLOW + "Members " + ChatColor.GOLD + "(" + memberCount + ")" + ChatColor.YELLOW + ": " + ChatColor.GRAY + members);

        switch (party.getAccessRestriction()) {
            case PUBLIC:
                sender.sendMessage(ChatColor.YELLOW + "Privacy: " + ChatColor.GREEN + "Open");
                break;
            case INVITE_ONLY:
                sender.sendMessage(ChatColor.YELLOW + "Privacy: " + ChatColor.GOLD + "Invite-Only");
                break;
            case PASSWORD:
                // leader can see password by hovering
                if (party.isLeader(sender.getUniqueId())) {
                    HoverEvent.Action showText = HoverEvent.Action.SHOW_TEXT;
                    BaseComponent[] passwordComponent = { new TextComponent(party.getPassword()) };

                    // Privacy: Password Protected [Hover for password]
                    ComponentBuilder builder = new ComponentBuilder("Privacy: ").color(ChatColor.YELLOW);
                    builder.append("Password Protected ").color(ChatColor.RED);
                    builder.append("[Hover for password]").color(ChatColor.GRAY);
                    builder.event(new HoverEvent(showText, passwordComponent));

                    sender.spigot().sendMessage(builder.create());
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "Privacy: " + ChatColor.RED + "Password Protected");
                }

                break;
            default:
                break;
        }

        sender.sendMessage(ChatColor.GRAY + PotPvPLang.LONG_LINE);
    }

}