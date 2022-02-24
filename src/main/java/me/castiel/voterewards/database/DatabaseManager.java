package me.castiel.voterewards.database;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager {

    private final Plugin plugin;
    private final File playersFolder;
    private final HashMap<String, Database> databasesMap = new HashMap<>();
    private final Database mainDatabase;

    public DatabaseManager(Plugin plugin, File dataFolder, String dir) {
        this.plugin = plugin;
        if (!dataFolder.exists()) {
            if (dataFolder.mkdir()) {
                Bukkit.getLogger().info("[CasUtils] Created parent directory.");
            }
        }
        playersFolder = new File(dataFolder.getPath() + "/" + dir);
        if (!playersFolder.exists()) {
            if (playersFolder.mkdir()) {
                Bukkit.getLogger().info("[CasUtils] Created sub directory (" + dir + ").");
            }
        }
        mainDatabase = new Database(playersFolder, "db.yml");
        autoSave();
    }

    private void autoSave() {
        new BukkitRunnable() {
            @Override
            public void run() {
                saveDatabases();
            }
        }.runTaskTimer(plugin, 300L * 20L, 300L * 20L);
    }

    private Database createYamlDatabase(String filename) {
        return new Database(playersFolder, filename + ".yml");
    }

    public void saveDatabases() {
        for (Map.Entry<String, Database> entry : databasesMap.entrySet()) {
            entry.getValue().save();
        }
        mainDatabase.save();
    }

    public Database getConfig(String identifier) {
        if (databasesMap.containsKey(identifier)) {
            return databasesMap.get(identifier);
        }
        Database database = createYamlDatabase(identifier);
        databasesMap.put(identifier, database);
        return database;
    }

    public void removeConfig(String identifier) {
        databasesMap.get(identifier).save();
        databasesMap.remove(identifier);
    }

    public Database getMainDatabase() {
        return mainDatabase;
    }
}