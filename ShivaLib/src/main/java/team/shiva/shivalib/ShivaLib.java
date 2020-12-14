package team.shiva.shivalib;

import lombok.Getter;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public class ShivaLib extends JavaPlugin {
    @Getter private static ShivaLib instance;
    private Logger logger;
    @Override
    public void onEnable() {
        instance = this;
        logger = this.getLogger();
        logger.info("ShivaLib has been enabled.");
    }

    @Override
    public void onDisable() {
        instance = null;
        logger.info("ShivaLib has been disabled.");
        logger = null;
    }
}
