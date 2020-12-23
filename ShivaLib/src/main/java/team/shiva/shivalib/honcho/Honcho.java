package team.shiva.shivalib.honcho;

import team.shiva.shivalib.ShivaLib;
import team.shiva.shivalib.honcho.command.CommandMeta;
import team.shiva.shivalib.honcho.command.CommandOption;
import team.shiva.shivalib.honcho.command.adapter.CommandTypeAdapter;
import team.shiva.shivalib.honcho.command.adapter.impl.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Honcho implements Listener {

    private final JavaPlugin plugin;
    private final Map<Class, CommandTypeAdapter> adapters;
    private final Map<String, Object> commands;
    private final Map<Object, CommandMeta> metas;

    public Honcho(JavaPlugin plugin) {
        this.plugin = plugin;

        this.adapters = new HashMap<>();
        this.commands = new HashMap<>();
        this.metas = new HashMap<>();

        registerTypeAdapter(Player.class, new PlayerTypeAdapter());
        registerTypeAdapter(String.class, new StringTypeAdapter());
        registerTypeAdapter(Number.class, new NumberTypeAdapter());
        registerTypeAdapter(int.class, new NumberTypeAdapter());
        registerTypeAdapter(long.class, new NumberTypeAdapter());
        registerTypeAdapter(double.class, new NumberTypeAdapter());
        registerTypeAdapter(float.class, new NumberTypeAdapter());
        registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        registerTypeAdapter(boolean.class, new BooleanTypeAdapter());
        registerTypeAdapter(World.class, new WorldTypeAdapter());
        registerTypeAdapter(GameMode.class, new GameModeTypeAdapter());
        registerTypeAdapter(CommandOption.class, new CommandOptionTypeAdapter());

        Bukkit.getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onServerCommandEvent(ServerCommandEvent event) {
        handle(event.getSender(), "/" + event.getCommand(), event);
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        handle(event.getPlayer(), event.getMessage(), event);
    }

    private void handle(CommandSender commandSender, String message, Cancellable cancellable) {
        String[] messageSplit = message.substring(1).split(" ");
        Object command = null;
        String label = null;

        for (int remaining = messageSplit.length; remaining > 0; remaining--) {
            label = StringUtils.join(messageSplit, " ", 0, remaining);

            if (commands.get(label.toLowerCase()) != null) {
                command = commands.get(label.toLowerCase());
                break;
            }
        }

        if (command != null) {
            CommandMeta meta = metas.get(command);
            String[] labelSplit = label.split(" ");
            String[] args = new String[0];

            if (messageSplit.length != labelSplit.length) {
                int numArgs = messageSplit.length - labelSplit.length;
                args = new String[numArgs];
                System.arraycopy(messageSplit, labelSplit.length, args,0, numArgs);
            }

            cancellable.setCancelled(true);

            HonchoExecutor executor = new HonchoExecutor(this, label.toLowerCase(), commandSender, command, args);

            if (meta.async()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        executor.execute();
                    }
                }.runTaskAsynchronously(plugin);
            } else {
                executor.execute();
            }
        }
    }

    public void registerTypeAdapter(Class clazz, CommandTypeAdapter adapter) {
        adapters.put(clazz, adapter);
    }

    public CommandTypeAdapter getTypeAdapter(Class clazz) {
        return adapters.get(clazz);
    }

    public void registerCommand(Object object) {
        CommandMeta meta = object.getClass().getAnnotation(CommandMeta.class);

        if (meta == null) {
            throw new RuntimeException(new ClassNotFoundException(object.getClass().getName() + " is missing CommandMeta annotation"));
        }

        for (String label : getLabels(object.getClass(), new ArrayList<>())) {
            commands.put(label.toLowerCase(), object);
        }

        metas.put(object, meta);

        if (meta.autoAddSubCommands()) {
            for (Class<?> clazz : object.getClass().getDeclaredClasses()) {
                if (clazz.getSuperclass().equals(object.getClass())) {
                    try {
                        registerCommand(clazz.getDeclaredConstructor(object.getClass()).newInstance(object));
                    } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private List<String> getLabels(Class clazz, List<String> list) {
        List<String> toReturn = new ArrayList<>();
        Class superClass = clazz.getSuperclass();

        if (superClass != null) {
            CommandMeta meta = (CommandMeta) superClass.getAnnotation(CommandMeta.class);

            if (meta != null) {
                list = getLabels(superClass, list);
            }
        }

        CommandMeta meta = (CommandMeta) clazz.getAnnotation(CommandMeta.class);

        if (meta == null) {
            return list;
        }

        if (list.isEmpty()) {
            toReturn.addAll(Arrays.asList(meta.label()));
        } else {
            for (String prefix : list) {
                for (String label : meta.label()) {
                    toReturn.add(prefix + " " + label);
                }
            }
        }

        return toReturn;
    }

}
