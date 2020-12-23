package team.shiva.shivalib.phoenix.lang.file.language;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import team.shiva.shivalib.phoenix.lang.file.AbstractConfigurationFile;

public class LanguageConfigurationFile
extends AbstractConfigurationFile {
    private static final LanguageConfigurationFileLocale DEFAULT_LOCALE = LanguageConfigurationFileLocale.ENGLISH;
    private final Map<LanguageConfigurationFileLocale, YamlConfiguration> configurations = new HashMap<LanguageConfigurationFileLocale, YamlConfiguration>();

    public LanguageConfigurationFile(JavaPlugin plugin, String name, boolean overwrite) {
        super(plugin, name);
        for (LanguageConfigurationFileLocale locale : LanguageConfigurationFileLocale.values()) {
            File file = new File(plugin.getDataFolder(), name + "_" + locale.getAbbreviation() + ".yml");
            String path = name + "_" + locale.getAbbreviation() + ".yml";
            if (plugin.getResource(path) == null) continue;
            plugin.saveResource(path, overwrite);
            this.configurations.put(locale, YamlConfiguration.loadConfiguration(file));
        }
    }

    public LanguageConfigurationFile(JavaPlugin plugin, String name) {
        this(plugin, name, false);
    }

    public List<String> replace(List<String> list, int position, Object argument) {
        ArrayList<String> toReturn = new ArrayList<>();
        for (String string : list) {
            toReturn.add(string.replace("{" + position + "}", argument.toString()));
        }
        return toReturn;
    }

    public List<String> replace(List<String> list, int position, Object ... arguments) {
        return this.replace(list, 0, position, arguments);
    }

    public List<String> replace(List<String> list, int index, int position, Object ... arguments) {
        ArrayList<String> toReturn = new ArrayList<>();
        for (String string : list) {
            for (int i = 0; i < arguments.length; ++i) {
                toReturn.add(string.replace("{" + position + "}", arguments[index + i].toString()));
            }
        }
        return toReturn;
    }

    public List<String> getStringListWithArgumentsOrRemove(String path, LanguageConfigurationFileLocale locale, Object ... arguments) {
        ArrayList<String> toReturn = new ArrayList<>();
        block0: for (String string : this.getStringList(path, locale)) {
            for (int i = 0; i < arguments.length; ++i) {
                if (!string.contains("{" + i + "}")) continue;
                Object object = arguments[i];
                if (object == null) continue block0;
                if (object instanceof List) {
                    for (Object obj : (List)object) {
                        if (!(obj instanceof String)) continue;
                        toReturn.add((String)obj);
                    }
                    continue block0;
                }
                string = string.replace("{" + i + "}", object.toString());
            }
            toReturn.add(string);
        }
        return toReturn;
    }

    public int indexOf(List<String> list, int position) {
        for (int i = 0; i < list.size(); ++i) {
            if (!list.get(i).contains("{" + position + "}")) continue;
            return i;
        }
        return -1;
    }

    public String getString(String path, LanguageConfigurationFileLocale locale) {
        if (!this.configurations.containsKey(locale)) {
            return locale == DEFAULT_LOCALE ? null : this.getString(path, DEFAULT_LOCALE);
        }
        YamlConfiguration configuration = this.configurations.get(locale);
        if (configuration.contains(path)) {
            return ChatColor.translateAlternateColorCodes('&', configuration.getString(path));
        }
        return null;
    }

    public String getString(String path, LanguageConfigurationFileLocale locale, Object ... arguments) {
        String toReturn = this.getString(path, locale);
        if (toReturn != null) {
            for (int i = 0; i < arguments.length; ++i) {
                toReturn = toReturn.replace("{" + i + "}", arguments[i].toString());
            }
            return toReturn;
        }
        return null;
    }

    @Override
    public String getString(String path) {
        return this.getString(path, DEFAULT_LOCALE);
    }

    public String getStringOrDefault(String path, String or, LanguageConfigurationFileLocale locale) {
        String toReturn = this.getString(path, locale);
        if (toReturn == null) {
            return or;
        }
        return toReturn;
    }

    @Override
    public String getStringOrDefault(String path, String or) {
        return this.getStringOrDefault(path, or, DEFAULT_LOCALE);
    }

    @Override
    public int getInteger(String path) {
        throw new UnsupportedOperationException("");
    }

    @Override
    @Deprecated
    public double getDouble(String path) {
        throw new UnsupportedOperationException("");
    }

    @Override
    @Deprecated
    public Object get(String path) {
        throw new UnsupportedOperationException("");
    }

    public List<String> getStringList(String path, LanguageConfigurationFileLocale locale, Object ... arguments) {
        ArrayList<String> toReturn = new ArrayList<>();
        block0: for (String line : this.getStringList(path, locale)) {
            for (int i = 0; i < arguments.length; ++i) {
                Object object = arguments[i];
                if (object instanceof List && line.contains("{" + i + "}")) {
                    for (Object obj : (List)object) {
                        if (!(obj instanceof String)) continue;
                        toReturn.add(line.replace("{" + i + "}", "") + obj);
                    }
                    continue block0;
                }
                line = line.replace("{" + i + "}", arguments[i].toString());
            }
            toReturn.add(line);
        }
        return toReturn;
    }

    public List<String> getStringList(String path, LanguageConfigurationFileLocale locale) {
        if (!this.configurations.containsKey(locale)) {
            return locale == DEFAULT_LOCALE ? null : this.getStringList(path, DEFAULT_LOCALE);
        }
        YamlConfiguration configuration = this.configurations.get(locale);
        if (configuration.contains(path)) {
            ArrayList<String> toReturn = new ArrayList<>();
            for (String string : configuration.getStringList(path)) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', string));
            }
            return toReturn;
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getStringList(String path) {
        return this.getStringList(path, DEFAULT_LOCALE);
    }

    public Map<LanguageConfigurationFileLocale, YamlConfiguration> getConfigurations() {
        return this.configurations;
    }
}

