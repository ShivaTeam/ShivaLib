package us.zonix.anticheat.log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import team.shiva.shivalib.util.Callback;
import team.shiva.shivalib.util.UUIDUtils;
import us.zonix.anticheat.LordMeme;

import java.util.UUID;

public class LogRequestFecther {

    private CommandSender sender;

    private UUID uuid;
    private String name;

    public LogRequestFecther(CommandSender sender, String name) {
        this.sender = sender;
        this.name = name;
        this.uuid = UUIDUtils.uuid(name);
        if(uuid == null){
            sender.sendMessage(ChatColor.RED + "Failed to find that player.");
        }
        name = UUIDUtils.name(uuid);
        FetchLog();
    }

    private void FetchLog(){
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "----- " + ChatColor.YELLOW + name + "'s logs" +ChatColor.GRAY + ChatColor.STRIKETHROUGH +" -----");
        MongoCollection<Document> mongoCollection = LordMeme.getInstance().getMongoDatabase().getCollection(uuid.toString());
        for(Document document: mongoCollection.find()){
            sender.sendMessage(document.getString("time")+" "+document.getString("message"));
        }
    }

}
