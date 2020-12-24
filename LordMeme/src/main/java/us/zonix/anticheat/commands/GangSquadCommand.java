package us.zonix.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.shiva.shivalib.honcho.command.CommandMeta;
import us.zonix.anticheat.LordMeme;

@CommandMeta(label = "gangsquad", permission = "meme.help")
public class GangSquadCommand {

    private LordMeme plugin = LordMeme.getInstance();

    public void onCommand(CommandSender player) {
        player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------");
        player.sendMessage(ChatColor.RED + "Lord_Meme Commands:");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/stfu - Regular alerts");
        player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/gtfo - Developer alerts");
        player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/rape - Ban someone");
        player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/skid - Watch someone");
        player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/rail - Misplace command");
        player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/setrangevl - Set range volume");
        player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/kys - Toggle a check");
        player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/banwave - Ban Wave executable");
        player.sendMessage(ChatColor.WHITE.toString() + ChatColor.BOLD + "* " + ChatColor.WHITE + "/logs - Display player logs");
        player.sendMessage(" ");
        player.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "* " + ChatColor.GREEN + "Server is running Lord_MeMe version " + ChatColor.RED + "¯\\_(ツ)_/¯");
        player.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.STRIKETHROUGH + "------------------------------------------");
    }

}
