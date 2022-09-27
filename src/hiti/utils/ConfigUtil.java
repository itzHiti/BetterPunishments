package hiti.utils;

import hiti.main.*;
import org.bukkit.configuration.file.*;
import java.io.*;
import net.md_5.bungee.api.*;

public class ConfigUtil {

    public static FileConfiguration getFile(final String fileName) {
        final File file = new File(BetterPunishments.getInstance().getDataFolder(), fileName);
        if (BetterPunishments.getInstance().getResource(fileName) == null) {
            try {
                YamlConfiguration.loadConfiguration(file).save(file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return (FileConfiguration)YamlConfiguration.loadConfiguration(file);
        }
        if (!file.exists()) {
            BetterPunishments.getInstance().saveResource(fileName, false);
        }
        return (FileConfiguration)YamlConfiguration.loadConfiguration(file);
    }

    public static String getMSG(final String path) {
        return ChatColor.translateAlternateColorCodes('&', BetterPunishments.messages.getString("Messages." + path).replace("{new}", "\n"));
    }

    public static String getMSGHover(final String path) {
        return ChatColor.translateAlternateColorCodes('&', BetterPunishments.messages.getString("Hover-messages." + path).replace("{new}", "\n"));
    }

    public static String getMSGInfo(final String path) {
        return ChatColor.translateAlternateColorCodes('&', BetterPunishments.messages.getString("Info-messages." + path).replace("{new}", "\n"));
    }
}
