package me.castiel.voterewards;

import me.castiel.voterewards.commands.CommandsManager;
import me.castiel.voterewards.configs.Messages;
import me.castiel.voterewards.configs.Shop;
import me.castiel.voterewards.database.DatabaseManager;
import me.castiel.voterewards.listeners.PlayerJoinEventListener;
import me.castiel.voterewards.listeners.VotifierEventListener;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class VoteRewards extends JavaPlugin {

    private static VoteRewards voteShop;
    private Messages messages;
    private Shop shop;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage("[VoteRewards] VoteRewards Loading...");
        saveDefaultConfig();
        voteShop = this;
        saveResource("messages.yml", false);
        saveResource("voteshop.yml", false);
        saveResource("commands.yml", false);
        databaseManager = new DatabaseManager(this, getDataFolder(), "data");
        new BukkitRunnable() {
            @Override
            public void run() {
                messages = new Messages(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "messages.yml")), getConfig());
                shop = new Shop(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "voteshop.yml")));
                new CommandsManager(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "commands.yml")));
                PluginManager pm = getServer().getPluginManager();
                pm.registerEvents(new PlayerJoinEventListener(), voteShop);
                pm.registerEvents(new VotifierEventListener(), voteShop);
                console.sendMessage("[VoteRewards] VoteShop Loaded :)");
            }
        }.runTaskLater(this, 100);
    }

    @Override
    public void onDisable() {
        databaseManager.saveDatabases();
    }

    public static VoteRewards getInstance() {
        return voteShop;
    }

    public Messages getMessages() {
        return messages;
    }

    public Inventory getVoteShop() {
        return shop.getInventory();
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
