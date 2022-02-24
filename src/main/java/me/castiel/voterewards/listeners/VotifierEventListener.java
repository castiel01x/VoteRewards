package me.castiel.voterewards.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.castiel.voterewards.VoteRewards;
import me.castiel.voterewards.configs.Messages;
import me.castiel.voterewards.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class VotifierEventListener implements Listener {

    @EventHandler
    public void onVote(VotifierEvent event) {
        Vote vote = event.getVote();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(vote.getUsername());
        if (!offlinePlayer.hasPlayedBefore()) return;

        VoteRewards plugin = VoteRewards.getInstance();
        Messages messages = plugin.getMessages();
        FileConfiguration mainCFG = plugin.getConfig();
        DatabaseManager dbManager = plugin.getDatabaseManager();
        FileConfiguration db = dbManager.getConfig(offlinePlayer.getUniqueId().toString()).getConfig();
        db.set("last-vote", System.currentTimeMillis());
        db.set("balance", db.getInt("balance", 0) + mainCFG.getInt("vote-shop.points-per-vote", 1));
        Bukkit.broadcastMessage(messages.voteBroadcastMessage.replace("%player%", vote.getUsername()));
        FileConfiguration mainDB = dbManager.getMainDatabase().getConfig();
        int vote_party = mainDB.getInt("vote-party", 0) + 1;
        mainDB.set("vote-party", vote_party);

        if (mainCFG.getBoolean("votes.vote-party.enabled", false)) {
            if (vote_party < mainCFG.getInt("vote-party.votes-required"))
                return;
            boolean broadcast = mainCFG.getBoolean("vote-party.broadcast-voteparty", false);
            if (broadcast)
                Bukkit.broadcastMessage(messages.votePartyBroadcastMessage);
            new BukkitRunnable() {
                @Override
                public void run() {
                    List<String> commands = mainCFG.getStringList("vote-party.commands");
                    ConsoleCommandSender cmdSender = plugin.getServer().getConsoleSender();
                    for (String command : commands) {
                        Bukkit.dispatchCommand(cmdSender, command);
                    }
                    if (broadcast)
                        Bukkit.broadcastMessage(messages.votePartyEndBroadcastMessage);
                }
            }.runTaskLater(plugin, mainCFG.getInt("vote-party.delay", 1) * 20L);
        }

        boolean rewards = mainCFG.getBoolean("votes.vote-rewards.enabled", false);
        if (offlinePlayer.isOnline()) {
            offlinePlayer.getPlayer().sendMessage(messages.playerVoteRewardMessage);
            if (!rewards) return;
            List<String> commands = mainCFG.getStringList("votes.vote-rewards.commands");
            ConsoleCommandSender cmdSender = plugin.getServer().getConsoleSender();
            for (String command : commands) {
                Bukkit.dispatchCommand(cmdSender, command.replace("%player%", offlinePlayer.getName()));
            }
        }
        else {
            if (!rewards) return;
            List<String> commands = mainCFG.getStringList("votes.vote-rewards.commands");
            List<String> oldUnclaimedCommands = db.getStringList("unclaimed-rewards");
            List<String> allUnclaimed = new ArrayList<>();
            allUnclaimed.addAll(commands);
            allUnclaimed.addAll(oldUnclaimedCommands);
            db.set("unclaimed-rewards", allUnclaimed);
        }
    }
}
