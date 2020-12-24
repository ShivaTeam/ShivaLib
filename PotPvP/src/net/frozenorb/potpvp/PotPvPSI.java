package net.frozenorb.potpvp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.qrakn.morpheus.Morpheus;
import com.qrakn.morpheus.game.Game;
import com.qrakn.morpheus.game.GameListeners;
import com.qrakn.morpheus.game.GameQueue;
import com.qrakn.morpheus.game.event.GameEvent;
import lombok.Getter;
import net.frozenorb.potpvp.arena.ArenaHandler;
import net.frozenorb.potpvp.command.*;
import net.frozenorb.potpvp.duel.DuelHandler;
import net.frozenorb.potpvp.elo.EloHandler;
import net.frozenorb.potpvp.follow.FollowHandler;
import net.frozenorb.potpvp.kit.KitHandler;
import net.frozenorb.potpvp.kittype.KitType;
import net.frozenorb.potpvp.kittype.KitTypeJsonAdapter;
import net.frozenorb.potpvp.kittype.command.KitLoadDefaultCommand;
import net.frozenorb.potpvp.kittype.command.KitSaveDefaultCommand;
import net.frozenorb.potpvp.kittype.command.KitWipeKitsCommands;
import net.frozenorb.potpvp.listener.*;
import net.frozenorb.potpvp.lobby.LobbyHandler;
import net.frozenorb.potpvp.match.Match;
import net.frozenorb.potpvp.match.MatchHandler;
import net.frozenorb.potpvp.morpheus.EventListeners;
import net.frozenorb.potpvp.morpheus.EventTask;
import net.frozenorb.potpvp.morpheus.command.ForceEndCommand;
import net.frozenorb.potpvp.morpheus.command.HostCommand;
import net.frozenorb.potpvp.nametag.PotPvPNametagProvider;
import net.frozenorb.potpvp.party.PartyHandler;
import net.frozenorb.potpvp.postmatchinv.PostMatchInvHandler;
import net.frozenorb.potpvp.pvpclasses.PvPClassHandler;
import net.frozenorb.potpvp.queue.QueueHandler;
import net.frozenorb.potpvp.rematch.RematchHandler;
import net.frozenorb.potpvp.scoreboard.PotPvPScoreboardConfiguration;
import net.frozenorb.potpvp.setting.SettingHandler;
import net.frozenorb.potpvp.statistics.StatisticsHandler;
import net.frozenorb.potpvp.tab.PotPvPLayoutProvider;
import net.frozenorb.potpvp.tournament.TournamentHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import team.shiva.shivalib.ShivaLib;
import team.shiva.shivalib.nametag.NametagHandler;
import team.shiva.shivalib.scoreboard.ScoreboardHandler;
import team.shiva.shivalib.serialization.*;
import team.shiva.shivalib.tablist.TablistHandler;

import java.io.IOException;

public final class PotPvPSI extends JavaPlugin {

    private static PotPvPSI instance;
    @Getter private static final Gson gson = new GsonBuilder()
        .registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter())
        .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
        .registerTypeHierarchyAdapter(Location.class, new LocationAdapter())
        .registerTypeHierarchyAdapter(Vector.class, new VectorAdapter())
        .registerTypeAdapter(BlockVector.class, new BlockVectorAdapter())
        .registerTypeHierarchyAdapter(KitType.class, new KitTypeJsonAdapter()) // custom KitType serializer
        .serializeNulls()
        .create();

    @Getter private MongoClient mongoClient;
    @Getter private MongoDatabase mongoDatabase;

    @Getter private SettingHandler settingHandler;
    @Getter private DuelHandler duelHandler;
    @Getter private KitHandler kitHandler;
    @Getter private LobbyHandler lobbyHandler;
    private ArenaHandler arenaHandler;
    @Getter private MatchHandler matchHandler;
    @Getter private PartyHandler partyHandler;
    @Getter private QueueHandler queueHandler;
    @Getter private RematchHandler rematchHandler;
    @Getter private PostMatchInvHandler postMatchInvHandler;
    @Getter private FollowHandler followHandler;
    @Getter private EloHandler eloHandler;
    @Getter private TournamentHandler tournamentHandler;
    @Getter private PvPClassHandler pvpClassHandler;
    
    @Getter private final ChatColor dominantColor = ChatColor.RED;

    @Override
    public void onEnable() {
        //SpigotConfig.onlyCustomTab = true; // because we'll definitely forget
        //this.dominantColor = ChatColor.DARK_PURPLE;
        instance = this;
        saveDefaultConfig();

        setupMongo();

        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setTime(6_000L);
        }

        settingHandler = new SettingHandler();
        duelHandler = new DuelHandler();
        kitHandler = new KitHandler();
        lobbyHandler = new LobbyHandler();
        arenaHandler = new ArenaHandler();
        matchHandler = new MatchHandler();
        partyHandler = new PartyHandler();
        queueHandler = new QueueHandler();
        rematchHandler = new RematchHandler();
        postMatchInvHandler = new PostMatchInvHandler();
        followHandler = new FollowHandler();
        eloHandler = new EloHandler();
        pvpClassHandler = new PvPClassHandler();
        tournamentHandler = new TournamentHandler();

        new Morpheus(this); // qrakn game events
        new EventTask().runTaskTimerAsynchronously(this, 1L, 1L);

        for (GameEvent event : GameEvent.getEvents()) {
            for (Listener listener : event.getListeners()) {
                getServer().getPluginManager().registerEvents(listener, this);
            }
        }

        getServer().getPluginManager().registerEvents(new BasicPreventionListener(), this);
        getServer().getPluginManager().registerEvents(new BowHealthListener(), this);
        getServer().getPluginManager().registerEvents(new ChatFormatListener(), this);
        getServer().getPluginManager().registerEvents(new NightModeListener(), this);
        getServer().getPluginManager().registerEvents(new PearlCooldownListener(), this);
        getServer().getPluginManager().registerEvents(new RankedMatchQualificationListener(), this);
        getServer().getPluginManager().registerEvents(new TabCompleteListener(), this);
        getServer().getPluginManager().registerEvents(new StatisticsHandler(), this);
        getServer().getPluginManager().registerEvents(new GameListeners(), this);
        getServer().getPluginManager().registerEvents(new EventListeners(), this);

        ShivaLib.getInstance().getHoncho().registerCommand(new BuildCommand.Silent());
        ShivaLib.getInstance().getHoncho().registerCommand(new EditPotionModifyCommand.EditPotionModify());
        ShivaLib.getInstance().getHoncho().registerCommand(new EloConvertCommand.Eloconvert());
        ShivaLib.getInstance().getHoncho().registerCommand(new HelpCommand.Help());
        ShivaLib.getInstance().getHoncho().registerCommand(new KillCommands.Suicide());
        ShivaLib.getInstance().getHoncho().registerCommand(new KillCommands.Kill());
        ShivaLib.getInstance().getHoncho().registerCommand(new ManageCommand.Manage());
        ShivaLib.getInstance().getHoncho().registerCommand(new MatchListCommand.MatchList());
        ShivaLib.getInstance().getHoncho().registerCommand(new MatchStatusCommand.MatchStatus());
        ShivaLib.getInstance().getHoncho().registerCommand(new PingCommand.Ping());
        ShivaLib.getInstance().getHoncho().registerCommand(new PStatusCommand.PStatus());
        ShivaLib.getInstance().getHoncho().registerCommand(new SilentCommand.Silent());
        ShivaLib.getInstance().getHoncho().registerCommand(new UpdateInventoryCommand.UpdateInventory());
        ShivaLib.getInstance().getHoncho().registerCommand(new UpdateVisibilityCommands.UpdateVisibility());
        ShivaLib.getInstance().getHoncho().registerCommand(new UpdateVisibilityCommands.UpdateVisibilityFlicker());
        ShivaLib.getInstance().getHoncho().registerCommand(new KitLoadDefaultCommand.KitLoadDefault());
        ShivaLib.getInstance().getHoncho().registerCommand(new KitSaveDefaultCommand.KitSaveDefault());
        ShivaLib.getInstance().getHoncho().registerCommand(new KitWipeKitsCommands.KitWipeKitsType());
        ShivaLib.getInstance().getHoncho().registerCommand(new KitWipeKitsCommands.KitWipeKitsPlayer());
        ShivaLib.getInstance().getHoncho().registerCommand(new RankedMatchQualificationListener.RmqImport());
        ShivaLib.getInstance().getHoncho().registerCommand(new RankedMatchQualificationListener.RmqRead());
        ShivaLib.getInstance().getHoncho().registerCommand(new RankedMatchQualificationListener.RmqSet());
        ShivaLib.getInstance().getHoncho().registerCommand(new ForceEndCommand.ForceEnd());
        ShivaLib.getInstance().getHoncho().registerCommand(new HostCommand.Host());

        NametagHandler.registerProvider(new PotPvPNametagProvider());
        ScoreboardHandler.setConfiguration(PotPvPScoreboardConfiguration.create());
        TablistHandler.setLayoutProvider(new PotPvPLayoutProvider());

    }

    @Override
    public void onDisable() {
        for (Match match : this.matchHandler.getHostedMatches()) {
            if (match.getKitType().isBuildingAllowed()) match.getArena().restore();
        }

        GameQueue.INSTANCE.getCurrentGames().forEach(Game::end);

        try {
            arenaHandler.saveSchematics();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String playerName : PvPClassHandler.getEquippedKits().keySet()) {
            PvPClassHandler.getEquippedKits().get(playerName).remove(getServer().getPlayerExact(playerName));
        }

        instance = null;
    }

    private void setupMongo() {
        mongoClient = new MongoClient(
            getConfig().getString("Mongo.Host"),
            getConfig().getInt("Mongo.Port")
        );

        String databaseId = getConfig().getString("Mongo.Database");
        mongoDatabase = mongoClient.getDatabase(databaseId);
    }


    public ArenaHandler getArenaHandler() {
        return arenaHandler;
    }

    public static PotPvPSI getInstance() {
        return instance;
    }
}