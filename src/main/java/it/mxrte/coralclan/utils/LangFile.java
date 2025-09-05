package it.mxrte.coralclan.utils;

import it.mxrte.coralclan.CoralClan;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class LangFile {
    private static FileConfiguration messages;
    private static File files;
    private final JavaPlugin plugin;

    public LangFile(JavaPlugin plugin){
        this.plugin = plugin;
    }

    public void createConfig() {
        files = new File(plugin.getDataFolder(), "lang.yml");
        if (!files.exists()) {
            files.getParentFile().mkdirs();
            plugin.saveResource("lang.yml", false);
        }
        messages = new YamlConfiguration();
        try {
            messages.load(files);
        }
        catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return messages;
    }

    public void saveConfig() {
        try {
            messages.save(files);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
