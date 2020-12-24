package team.shiva.core.config.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.bukkit.configuration.file.FileConfiguration;

import team.shiva.core.config.ConfigConversion;

public class ConfigConversion4 implements ConfigConversion {

    @Override
    public void convert(File file, FileConfiguration fileConfiguration) {
        fileConfiguration.set("CONFIG_VERSION", 4);

        fileConfiguration.set("STAFF.REPORT_BROADCAST", Arrays.asList(
                "&9[R] &b[{3}] &r{1} &7reported by &r{0}",
                "   &9Reason&7: {2}"
        ));

        fileConfiguration.set("STAFF.REQUEST_BROADCAST", Arrays.asList(
                "&9[R] &b[{2}] &r{0} &7has requested assistance",
                "   &9Reason&7: {1}"
        ));

        fileConfiguration.set("STAFF.REQUEST_SUBMITTED", "&aYour request for assistance has been submitted to all available staff members.");

        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
