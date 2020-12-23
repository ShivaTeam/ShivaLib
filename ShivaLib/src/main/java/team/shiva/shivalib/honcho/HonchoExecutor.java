package team.shiva.shivalib.honcho;

import org.bukkit.Bukkit;
import team.shiva.shivalib.honcho.command.CPL;
import team.shiva.shivalib.honcho.command.CommandMeta;
import team.shiva.shivalib.honcho.command.CommandOption;
import team.shiva.shivalib.honcho.command.adapter.CommandTypeAdapter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class HonchoExecutor {

    private final Honcho honcho;
    private final String label;
    private final CommandMeta meta;
    private final CommandSender sender;
    private final Object command;
    private String[] args;

    public HonchoExecutor(Honcho honcho, String label, CommandSender sender, Object command, String[] args) {
        this.honcho = honcho;
        this.label = label;
        this.meta = command.getClass().getAnnotation(CommandMeta.class);
        this.sender = sender;
        this.command = command;
        this.args = args;
    }

    public void execute() {
        if (!meta.permission().equalsIgnoreCase("")) {
            if (!(sender.hasPermission(meta.permission()))) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
                return;
            }
        }

        outer: for (Method method : command.getClass().getMethods()) {

            if (!(method.getDeclaringClass().equals(command.getClass()))) {
                continue;
            }

            if (method.getParameterCount() - 1 > args.length) {

                boolean doContinue = true;
                for (Parameter parameter : method.getParameters()) {
                    if (parameter.getType().equals(CommandOption.class)) {
                        if (method.getParameterCount() - 2 <= args.length) {
                            doContinue = false;
                            break;
                        }
                    }
                }

                if (doContinue) {
                    continue;
                }
            }

            for (Method otherMethod : command.getClass().getMethods()) {
                if (!(otherMethod.equals(method))) {
                    if (method.getParameterCount() == otherMethod.getParameterCount()) {
                        if (method.getParameters()[0].getType().equals(CommandSender.class) && otherMethod.getParameters()[0].getType().equals(Player.class) && sender instanceof Player) {
                            continue outer;
                        }
                    }
                    if (args.length != method.getParameterCount() - 1) {
                        if (args.length - method.getParameterCount() > args.length - otherMethod.getParameterCount()) {
                            continue outer;
                        }
                    }
                }
            }

            if (method.getParameterCount() > 0 && (method.getParameters()[0].getType().equals(CommandSender.class) || method.getParameters()[0].getType().equals(Player.class))) {
                List<Object> arguments = new ArrayList<>();
                Parameter[] parameters = method.getParameters();

                arguments.add(sender);

                if (method.getParameters()[0].getType().equals(Player.class) && !(sender instanceof Player)) {
                    continue outer;
                }

                for (int i = 1; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    CommandTypeAdapter adapter = honcho.getTypeAdapter(parameter.getType());

                    if (adapter == null) {
                        // TODO: throw error or log?

                        arguments.add(null);
                        continue;
                    }

                    Object object;
                    if (i == (parameters.length - 1)) {
                        object = adapter.convert(StringUtils.join(args, " ", i-1, args.length), parameter.getType());
                    } else {
                        object = adapter.convert(args[i-1], parameter.getType());
                    }

                    if (parameter.getType().equals(CommandOption.class) && object == null) {
                        List<String> replacement = new ArrayList<>(Arrays.asList(args));
                        replacement.add(i-1, null);
                        args = replacement.toArray(new String[0]);
                    }

                    if (object instanceof CommandOption) {
                        CommandOption option = (CommandOption) object;
                        if (!(Arrays.asList(meta.options())).contains(option.getTag().toLowerCase())) {
                            sender.sendMessage(ChatColor.RED + "Unrecognized command option \"-" + option.getTag().toLowerCase() + "\"!");
                            break outer;
                        }
                    }

                    arguments.add(object);
                }

                if (arguments.size() == parameters.length) {
                    try {
                        method.invoke(command, arguments.toArray());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

        }

        sender.sendMessage(getUsage());
    }

    //TODO: finish
    private String getUsage() {
        StringBuilder builder = new StringBuilder();

        builder.append(ChatColor.RED).append("Usage: /").append(label);

        if (meta.options().length > 0) {
            List<String> options = new ArrayList<>();

            for (String option : meta.options()) {
                options.add("-" + option.toLowerCase());
            }

            builder.append(" [");
            builder.append(StringUtils.join(options, ","));
            builder.append("]");
        }

        Map<Integer, List<String>> arguments = new HashMap<>();

        for (Method method : command.getClass().getDeclaredMethods()) {
            Parameter[] parameters = method.getParameters();
            for (int i = 1; i < parameters.length; i++) {
                List<String> argument = arguments.getOrDefault(i - 1, new ArrayList<>());
                Parameter parameter = parameters[i];

                if (parameter.getType().equals(CommandOption.class)) {
                    arguments.put(i - 1, null);
                    continue;
                }

                if (parameter.isAnnotationPresent(CPL.class)) {
                    argument.add(parameter.getAnnotation(CPL.class).value().toLowerCase());
                } else {
                    String name = parameter.getName();
                    if (!(argument.contains(name))) {
                        argument.add(name);
                    }
                }

                arguments.put(i - 1, argument);
            }
        }

        for (int i = 0; i < arguments.size(); i++) {
            List<String> argument = arguments.get(i);

            if (argument != null) {
                builder.append(" <").append(StringUtils.join(argument, "/")).append(">");
            }
        }

        return builder.toString();
    }

}
