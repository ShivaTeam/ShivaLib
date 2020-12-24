package team.shiva.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

import team.shiva.core.cache.RedisCache;
import team.shiva.core.chat.Chat;
import team.shiva.core.chat.ChatListener;
import team.shiva.core.chat.command.ClearChatCommand;
import team.shiva.core.chat.command.MuteChatCommand;
import team.shiva.core.chat.command.SlowChatCommand;
import team.shiva.core.config.ConfigValidation;
import team.shiva.core.essentials.Essentials;
import team.shiva.core.essentials.EssentialsListener;
import team.shiva.core.essentials.command.*;
import team.shiva.core.network.NetworkPacketListener;
import team.shiva.core.network.command.ReportCommand;
import team.shiva.core.network.command.RequestCommand;
import team.shiva.core.network.packet.PacketAddGrant;
import team.shiva.core.network.packet.PacketBroadcastPunishment;
import team.shiva.core.network.packet.PacketClearGrants;
import team.shiva.core.network.packet.PacketClearPunishments;
import team.shiva.core.network.packet.PacketDeleteGrant;
import team.shiva.core.network.packet.PacketDeleteRank;
import team.shiva.core.network.packet.PacketRefreshRank;
import team.shiva.core.network.packet.PacketStaffChat;
import team.shiva.core.network.packet.PacketStaffJoinNetwork;
import team.shiva.core.network.packet.PacketStaffLeaveNetwork;
import team.shiva.core.network.packet.PacketStaffReport;
import team.shiva.core.network.packet.PacketStaffRequest;
import team.shiva.core.network.packet.PacketStaffSwitchServer;
import team.shiva.core.profile.Profile;
import team.shiva.core.profile.ProfileListener;
import team.shiva.core.profile.ProfileTypeAdapter;
import team.shiva.core.profile.conversation.command.MessageCommand;
import team.shiva.core.profile.conversation.command.ReplyCommand;
import team.shiva.core.profile.conversation.command.SayCommand;
import team.shiva.core.profile.grant.GrantListener;
import team.shiva.core.profile.grant.command.ClearGrantsCommand;
import team.shiva.core.profile.grant.command.GrantCommand;
import team.shiva.core.profile.grant.command.GrantsCommand;
import team.shiva.core.profile.option.command.TogglePrivateMessagesCommand;
import team.shiva.core.profile.option.command.ToggleSoundsCommand;
import team.shiva.core.profile.punishment.command.BanCommand;
import team.shiva.core.profile.punishment.command.CheckCommand;
import team.shiva.core.profile.punishment.command.ClearPunishmentsCommand;
import team.shiva.core.profile.punishment.command.KickCommand;
import team.shiva.core.profile.punishment.command.MuteCommand;
import team.shiva.core.profile.punishment.command.UnbanCommand;
import team.shiva.core.profile.punishment.command.UnmuteCommand;
import team.shiva.core.profile.punishment.command.WarnCommand;
import team.shiva.core.profile.punishment.listener.PunishmentListener;
import team.shiva.core.profile.staff.command.AltsCommand;
import team.shiva.core.profile.staff.command.StaffChatCommand;
import team.shiva.core.profile.staff.command.StaffModeCommand;
import team.shiva.core.rank.Rank;
import team.shiva.core.rank.RankTypeAdapter;
import team.shiva.core.rank.command.RankAddPermissionCommand;
import team.shiva.core.rank.command.RankCreateCommand;
import team.shiva.core.rank.command.RankDeleteCommand;
import team.shiva.core.rank.command.RankHelpCommand;
import team.shiva.core.rank.command.RankInfoCommand;
import team.shiva.core.rank.command.RankInheritCommand;
import team.shiva.core.rank.command.RankRemovePermissionCommand;
import team.shiva.core.rank.command.RankSetColorCommand;
import team.shiva.core.rank.command.RankSetPrefixCommand;
import team.shiva.core.rank.command.RankSetWeightCommand;
import team.shiva.core.rank.command.RankUninheritCommand;
import team.shiva.core.rank.command.RanksCommand;
import team.shiva.core.util.CC;
import team.shiva.core.util.adapter.ChatColorTypeAdapter;
import team.shiva.core.util.duration.Duration;
import team.shiva.core.util.duration.DurationTypeAdapter;
import team.shiva.core.util.menu.MenuListener;
import team.shiva.shivalib.honcho.Honcho;
import team.shiva.shivalib.phoenix.lang.file.type.BasicConfigurationFile;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.evilblock.pidgin.Pidgin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Core extends JavaPlugin {

	public static final Gson GSON = new Gson();
	public static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {}.getType();

	private static Core core;

	@Getter private BasicConfigurationFile mainConfig;

	@Getter private Honcho honcho;
	@Getter private Pidgin pidgin;

	@Getter private MongoClient mongoClient;
	@Getter private MongoDatabase mongoDatabase;
	@Getter private JedisPool jedisPool;
	@Getter private RedisCache redisCache;

	@Getter private Essentials essentials;
	@Getter private Chat chat;

	@Getter @Setter private boolean debug;

	@Override
	public void onEnable() {
		core = this;
		
		mainConfig = new BasicConfigurationFile(this, "config");

		new ConfigValidation(mainConfig.getFile(), mainConfig.getConfiguration(), 4).check();

		loadMongo();
		loadRedis();

		redisCache = new RedisCache(this);
		essentials = new Essentials(this);
		chat = new Chat(this);

		honcho = new Honcho(this);

		Arrays.asList(
				new BroadcastCommand(),
				new ClearCommand(),
				new GameModeCommand(),
				new HealCommand(),
				new ShowAllPlayersCommand(),
				new ShowPlayerCommand(),
				new HidePlayerCommand(),
				new LocationCommand(),
				new MoreCommand(),
				new RenameCommand(),
				new SetSlotsCommand(),
				new ClearChatCommand(),
				new SlowChatCommand(),
				new AltsCommand(),
				new BanCommand(),
				new CheckCommand(),
				new KickCommand(),
				new MuteCommand(),
				new UnbanCommand(),
				new UnmuteCommand(),
				new WarnCommand(),
				new GrantCommand(),
				new GrantsCommand(),
				new StaffChatCommand(),
				new StaffModeCommand(),
				new MuteChatCommand(),
				new RankAddPermissionCommand(),
				new RankCreateCommand(),
				new RankDeleteCommand(),
				new RankHelpCommand(),
				new RankInfoCommand(),
				new RankInheritCommand(),
				new RankRemovePermissionCommand(),
				new RanksCommand(),
				new RankSetColorCommand(),
				new RankSetPrefixCommand(),
				new RankSetWeightCommand(),
				new RankUninheritCommand(),
				new CoreDebugCommand(),
				new TeleportWorldCommand(),
				new MessageCommand(),
				new ReplyCommand(),
				new SayCommand(),
				new TogglePrivateMessagesCommand(),
				new ToggleSoundsCommand(),
				new ListCommand(),
				new ReportCommand(),
				new RequestCommand(),
				new ClearGrantsCommand(),
				new ClearPunishmentsCommand(),
				new SudoCommand(),
				new SudoAllCommand()
		).forEach(honcho::registerCommand);

		honcho.registerTypeAdapter(Rank.class, new RankTypeAdapter());
		honcho.registerTypeAdapter(Profile.class, new ProfileTypeAdapter());
		honcho.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
		honcho.registerTypeAdapter(ChatColor.class, new ChatColorTypeAdapter());

		pidgin = new Pidgin("core",
				mainConfig.getString("REDIS.HOST"),
				mainConfig.getInteger("REDIS.PORT"),
				mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED") ?
						mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD") : null
		);

		Arrays.asList(
				PacketAddGrant.class,
				PacketBroadcastPunishment.class,
				PacketDeleteGrant.class,
				PacketDeleteRank.class,
				PacketRefreshRank.class,
				PacketStaffChat.class,
				PacketStaffJoinNetwork.class,
				PacketStaffLeaveNetwork.class,
				PacketStaffSwitchServer.class,
				PacketStaffReport.class,
				PacketStaffRequest.class,
				PacketClearGrants.class,
				PacketClearPunishments.class
		).forEach(pidgin::registerPacket);

		pidgin.registerListener(new NetworkPacketListener(this));

		Arrays.asList(
				new ProfileListener(),
				new MenuListener(),
				new EssentialsListener(),
				new ChatListener(),
				new GrantListener(),
				new PunishmentListener()
		).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

		Rank.init();
		
		final int delay = mainConfig.getInteger("MESSAGES.DELAY");
		final int count = mainConfig.getInteger("MESSAGES.COUNT");
		List<String> list = new ArrayList<>();
		for(int i=1;i<=count;i++) {
			list.add(mainConfig.getString("MESSAGES.MESSAGE"+i));
		}
		new BukkitRunnable() {
			int cur=0;
			@Override
			public void run() {
				String str = list.get(cur);
				for(Player p:Bukkit.getOnlinePlayers()) {
					p.sendMessage(str);
				}
				if(++cur==count) {
					cur=0;
				}
			}
		}.runTaskTimer(this, delay, delay);
	}

	@Override
	public void onDisable() {
		try {
			jedisPool.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Broadcasts a message to all server operators.
	 *
	 * @param message The message.
	 */
	public static void broadcastOps(String message) {
		Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(op -> op.sendMessage(CC.translate(message)));
	}

	private void loadMongo() {
		if (mainConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
			ServerAddress serverAddress = new ServerAddress(mainConfig.getString("MONGO.HOST"),
					mainConfig.getInteger("MONGO.PORT"));

			MongoCredential credential = MongoCredential.createCredential(
					mainConfig.getString("MONGO.AUTHENTICATION.USERNAME"), "admin",
					mainConfig.getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray());
			mongoClient = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build());
			mongoDatabase = mongoClient.getDatabase("core");
		} else {
			mongoClient = new MongoClient(mainConfig.getString("MONGO.HOST"),mainConfig.getInteger("MONGO.PORT"));
			mongoDatabase = mongoClient.getDatabase("core");
		}
	}

	private void loadRedis() {
		jedisPool = new JedisPool(mainConfig.getString("REDIS.HOST"), mainConfig.getInteger("REDIS.PORT"));

		if (mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED")) {
			try (Jedis jedis = jedisPool.getResource()) {
				jedis.auth(mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD"));
			}
		}
	}

	public static Core get() {
		return core;
	}

}
