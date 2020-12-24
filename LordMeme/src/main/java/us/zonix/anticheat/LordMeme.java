package us.zonix.anticheat;

import com.minexd.spigot.SpigotX;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.plugin.java.JavaPlugin;
import team.shiva.shivalib.ShivaLib;
import us.zonix.anticheat.commands.*;
import us.zonix.anticheat.handler.CustomMovementHandler;
import us.zonix.anticheat.handler.CustomPacketHandler;
import us.zonix.anticheat.listener.BanWaveListener;
import us.zonix.anticheat.listener.BungeeListener;
import us.zonix.anticheat.listener.ModListListener;
import us.zonix.anticheat.listener.PlayerListener;
import us.zonix.anticheat.manager.AlertsManager;
import us.zonix.anticheat.manager.BanWaveManager;
import us.zonix.anticheat.manager.LogManager;
import us.zonix.anticheat.manager.PlayerDataManager;
import us.zonix.anticheat.runnable.ExportLogs;

public class LordMeme extends JavaPlugin
{
    private static LordMeme instance;
    private PlayerDataManager playerDataManager;
    private BanWaveManager banWaveManager;
    private AlertsManager alertsManager;
    private LogManager logManager;
    private double rangeVl;
    @Getter MongoClient mongoClient;
    @Getter MongoDatabase mongoDatabase;

    public LordMeme() {
        this.rangeVl = 30.0;
    }
    
    public void onEnable() {
        LordMeme.instance = this;
        this.saveDefaultConfig();
        this.setupMongo();
        this.registerHandlers();
        this.registerManagers();
        this.registerListeners();
        this.registerCommands();
        this.registerExportLogsTimer();
    }
    
    public boolean isAntiCheatEnabled() {
        return MinecraftServer.getServer().tps1.getAverage() <= 19.0 || MinecraftServer.LAST_TICK_TIME + 100L <= System.currentTimeMillis();
    }

    private void setupMongo() {
        mongoClient = new MongoClient(
                getConfig().getString("Mongo.Host"),
                getConfig().getInt("Mongo.Port")
        );

        String databaseId = getConfig().getString("Mongo.Database");
        mongoDatabase = mongoClient.getDatabase(databaseId);
    }
    
    private void registerHandlers() {
        SpigotX.INSTANCE.addPacketHandler(new CustomPacketHandler(this));
        SpigotX.INSTANCE.addMovementHandler(new CustomMovementHandler(this));

        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener(this));
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }
    
    private void registerManagers() {
        this.alertsManager = new AlertsManager(this);
        this.banWaveManager = new BanWaveManager(this);
        this.playerDataManager = new PlayerDataManager(this);
        this.logManager = new LogManager();
    }
    
    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new BanWaveListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ModListListener(this), this);
    }
    
    private void registerCommands() {
        ShivaLib.getInstance().getHoncho().registerCommand(new AlertsCommand());
        ShivaLib.getInstance().getHoncho().registerCommand(new DevAlertsCommand());
        ShivaLib.getInstance().getHoncho().registerCommand(new LogsCommand());
        ShivaLib.getInstance().getHoncho().registerCommand(new ToggleCommand());
        ShivaLib.getInstance().getHoncho().registerCommand(new BanCommand());
        ShivaLib.getInstance().getHoncho().registerCommand(new RangeCommand());
        ShivaLib.getInstance().getHoncho().registerCommand(new WatchCommand());
        ShivaLib.getInstance().getHoncho().registerCommand(new ExemptCommand());
        ShivaLib.getInstance().getHoncho().registerCommand(new MisplaceCommand());
        ShivaLib.getInstance().getHoncho().registerCommand(new BanWaveCommand());
        ShivaLib.getInstance().getHoncho().registerCommand(new GangSquadCommand());
    }
    
    private void registerExportLogsTimer() {
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new ExportLogs(this), 600L, 600L);
    }
    
    public PlayerDataManager getPlayerDataManager() {
        return this.playerDataManager;
    }
    
    public BanWaveManager getBanWaveManager() {
        return this.banWaveManager;
    }
    
    public AlertsManager getAlertsManager() {
        return this.alertsManager;
    }
    
    public LogManager getLogManager() {
        return this.logManager;
    }
    
    public static LordMeme getInstance() {
        return LordMeme.instance;
    }
    
    public double getRangeVl() {
        return this.rangeVl;
    }
    
    public void setRangeVl(final double rangeVl) {
        this.rangeVl = rangeVl;
    }
}
