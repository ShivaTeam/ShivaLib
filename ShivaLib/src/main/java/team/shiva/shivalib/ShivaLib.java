package team.shiva.shivalib;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import team.shiva.shivalib.autoreboot.AutoRebootHandler;
import team.shiva.shivalib.event.HalfHourEvent;
import team.shiva.shivalib.event.HourEvent;
import team.shiva.shivalib.honcho.Honcho;
import team.shiva.shivalib.nametag.NametagHandler;
import team.shiva.shivalib.phoenix.lang.file.type.BasicConfigurationFile;
import team.shiva.shivalib.redis.RedisCommand;
import team.shiva.shivalib.scoreboard.ScoreboardHandler;
import team.shiva.shivalib.serialization.*;
import team.shiva.shivalib.spawn.SpawnHandler;
import team.shiva.shivalib.tablist.TablistHandler;

import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ShivaLib extends JavaPlugin {
    @Getter private static ShivaLib instance;
    private Logger logger;
    @Getter private Honcho honcho;
    @Getter private BasicConfigurationFile mainConfig;
    public static Random RANDOM = new Random();
    public static Gson GSON = new GsonBuilder().registerTypeHierarchyAdapter(PotionEffect .class, new PotionEffectAdapter()).registerTypeHierarchyAdapter(ItemStack .class, new ItemStackAdapter()).registerTypeHierarchyAdapter(Location .class, new LocationAdapter()).registerTypeHierarchyAdapter(Vector .class, new VectorAdapter()).registerTypeAdapter(BlockVector .class, new BlockVectorAdapter()).setPrettyPrinting().serializeNulls().create();
    public static Gson PLAIN_GSON = new GsonBuilder().registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter()).registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter()).registerTypeHierarchyAdapter(Location.class, new LocationAdapter()).registerTypeHierarchyAdapter(Vector.class, new VectorAdapter()).registerTypeAdapter(BlockVector.class, new BlockVectorAdapter()).serializeNulls().create();
    @Getter private JedisPool localJedisPool;
    @Getter private JedisPool backboneJedisPool;
    @Getter
    TablistHandler tablistHandler;
    @Override
    public void onEnable() {
        instance = this;
        logger = this.getLogger();
        this.saveDefaultConfig();
        mainConfig = new BasicConfigurationFile(this, "config");
        try {
            this.localJedisPool = new JedisPool(new JedisPoolConfig(), mainConfig.getString("Redis.Host"), 6379, 20000, null, mainConfig.getInteger("Redis.DbId"));
        }
        catch (Exception e) {
            this.localJedisPool = null;
            e.printStackTrace();
            this.getLogger().warning("Couldn't connect to a Redis instance at " + mainConfig.getString("Redis.Host") + ".");
        }
        try {
            this.backboneJedisPool = new JedisPool(new JedisPoolConfig(), mainConfig.getString("BackboneRedis.Host"), 6379, 20000, null, mainConfig.getInteger("BackboneRedis.DbId"));
        }
        catch (Exception e) {
            this.backboneJedisPool = null;
            e.printStackTrace();
            this.getLogger().warning("Couldn't connect to a Backbone Redis instance at " + mainConfig.getString("BackboneRedis.Host") + ".");
        }
        honcho = new Honcho(this);
        AutoRebootHandler.init();
        NametagHandler.init();
        ScoreboardHandler.init();
        SpawnHandler.init();
        TablistHandler.init();
        setupHourEvents();
        logger.info("ShivaLib has been enabled.");
    }

    @Override
    public void onDisable() {
        instance = null;
        localJedisPool.close();
        backboneJedisPool.close();
        logger.info("ShivaLib has been disabled.");
        logger = null;
    }


    public <T> T runRedisCommand(RedisCommand<T> redisCommand) {
        Jedis jedis = this.localJedisPool.getResource();
        T result = null;
        try {
            result = redisCommand.execute(jedis);
        }
        catch (Exception e) {
            e.printStackTrace();
            if (jedis != null) {
                localJedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        }
        finally {
            if (jedis != null) {
                localJedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    public <T> T runBackboneRedisCommand(RedisCommand<T> redisCommand) {

        Jedis jedis = this.backboneJedisPool.getResource();
        T result = null;
        try {
            result = redisCommand.execute(jedis);
        }
        catch (Exception e) {
            e.printStackTrace();
            if (jedis != null) {
                backboneJedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        }
        finally {
            if (jedis != null) {
                backboneJedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    private void setupHourEvents() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("ShivaLib - Hour Event Thread").setDaemon(true).build());
        int minOfHour = Calendar.getInstance().get(Calendar.MINUTE);
        int minToHour = 60 - minOfHour;
        int minToHalfHour = minToHour >= 30 ? minToHour - 30 : minToHour;
        executor.scheduleAtFixedRate(() -> Bukkit.getScheduler().runTask(this, () -> Bukkit.getPluginManager().callEvent(new HourEvent(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)))), minToHour, 60L, TimeUnit.MINUTES);
        executor.scheduleAtFixedRate(() -> Bukkit.getScheduler().runTask(this, () -> Bukkit.getPluginManager().callEvent(new HalfHourEvent(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE)))), minToHalfHour, 30L, TimeUnit.MINUTES);
    }
}
