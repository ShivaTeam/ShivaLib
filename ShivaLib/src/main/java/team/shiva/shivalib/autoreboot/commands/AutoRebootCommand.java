package team.shiva.shivalib.autoreboot.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import team.shiva.shivalib.ShivaLib;
import team.shiva.shivalib.autoreboot.AutoRebootHandler;
import team.shiva.shivalib.honcho.command.CommandMeta;
import team.shiva.shivalib.util.TimeUtils;

import java.util.concurrent.TimeUnit;

public class AutoRebootCommand {
    @CommandMeta(label={"reboot"}, permission="server.reboot")
    public static class Reboot{
        public void execute(CommandSender sender, String time){
            reboot(sender, time);
        }
    }
    public static void reboot(CommandSender sender, String unparsedTime) {
        try {
            unparsedTime = unparsedTime.toLowerCase();
            int time = TimeUtils.parseTime(unparsedTime);
            AutoRebootHandler.rebootServer(time, TimeUnit.SECONDS);
            sender.sendMessage(ChatColor.YELLOW + "Started auto reboot.");
        }
        catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + ex.getMessage());
        }
    }

    @CommandMeta(label={"reboot cancel"}, permission="server.reboot")
    public static class RebootCancel{
        public void execute(CommandSender sender){
            rebootCancel(sender);
        }
    }
    public static void rebootCancel(CommandSender sender) {
        if (!AutoRebootHandler.isRebooting()) {
            sender.sendMessage(ChatColor.RED + "No reboot has been scheduled.");
        } else {
            AutoRebootHandler.cancelReboot();
            ShivaLib.getInstance().getServer().broadcastMessage(ChatColor.RED + "\u26a0 " + ChatColor.DARK_RED.toString() + (Object)ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0");
            ShivaLib.getInstance().getServer().broadcastMessage(ChatColor.RED + "The server reboot has been cancelled.");
            ShivaLib.getInstance().getServer().broadcastMessage(ChatColor.RED + "\u26a0 " + ChatColor.DARK_RED.toString() + (Object)ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0");
        }
    }
}

