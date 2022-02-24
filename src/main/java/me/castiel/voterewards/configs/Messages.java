package me.castiel.voterewards.configs;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Messages {

    public String voteBroadcastMessage;
    public String votePartyBroadcastMessage;
    public String votePartyEndBroadcastMessage;
    public String playerBalanceMessage;
    public String playerVoteRewardMessage;
    public String playerPurchasedItem;
    public String playerInsufficientFunds;
    public String votePartyMessage;
    public List<String> voteReminderMessage;
    public List<String> voteHelpMessage;
    public List<String> voteLinksMessage;

    public Messages(FileConfiguration messagesCFG, FileConfiguration mainCFG) {
        voteBroadcastMessage = mainCFG.getString("votes.broadcast-message", "").replace('&', ChatColor.COLOR_CHAR);
        votePartyBroadcastMessage = mainCFG.getString("vote-party.broadcast-message", "").replace('&', ChatColor.COLOR_CHAR);
        votePartyEndBroadcastMessage = mainCFG.getString("vote-party.broadcast-end-message", "").replace('&', ChatColor.COLOR_CHAR);
        playerBalanceMessage = messagesCFG.getString("playerBalanceMessage", "").replace('&', ChatColor.COLOR_CHAR);
        playerVoteRewardMessage = messagesCFG.getString("playerVoteRewardMessage", "").replace('&', ChatColor.COLOR_CHAR);
        playerPurchasedItem = messagesCFG.getString("playerPurchasedItem", "").replace('&', ChatColor.COLOR_CHAR);
        playerInsufficientFunds = messagesCFG.getString("playerInsufficientFunds", "").replace('&', ChatColor.COLOR_CHAR);
        votePartyMessage = messagesCFG.getString("votePartyMessage", "").replace('&', ChatColor.COLOR_CHAR);
        voteReminderMessage = mainCFG.getStringList("votes.vote-reminder");
        voteReminderMessage.replaceAll(s -> s.replace('&', ChatColor.COLOR_CHAR));
        voteHelpMessage = messagesCFG.getStringList("voteHelpMessage");
        voteHelpMessage.replaceAll(s -> s.replace('&', ChatColor.COLOR_CHAR));
        voteLinksMessage = messagesCFG.getStringList("voteLinksMessage");
        voteLinksMessage.replaceAll(s -> s.replace('&', ChatColor.COLOR_CHAR));
    }
}
