package me.castiel.voterewards.listeners;

import me.castiel.voterewards.VoteRewards;
import me.castiel.voterewards.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class PlayerJoinEventListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        VoteRewards plugin = VoteRewards.getInstance();
        DatabaseManager dbManager = plugin.getDatabaseManager();
        FileConfiguration db = dbManager.getConfig(player.getUniqueId().toString()).getConfig();
        FileConfiguration mainCFG = plugin.getConfig();
        if (mainCFG.getBoolean("votes.vote-reminder.enabled", false)) {
            if (db.getLong("last-vote", 0L) > System.currentTimeMillis() - 86400000L) return;
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<String> messages = plugin.getMessages().voteReminderMessage;
                    for (String message : messages) {
                        player.sendMessage(message);
                    }
                }
            }.runTaskLater(plugin, mainCFG.getInt("votes.vote-reminder.delay", 1) * 20L);
        }

        List<String> commands = db.getStringList("unclaimed-rewards");
        if (commands.isEmpty()) return;
        ConsoleCommandSender cmdSender = plugin.getServer().getConsoleSender();
        for (String command : commands) {
            Bukkit.dispatchCommand(cmdSender, command.replace("%player%", player.getName()));
        }
        player.sendMessage(ChatColor.GREEN + "You received all of the unclaimed rewards, Thanks for voting!");
    }
}
