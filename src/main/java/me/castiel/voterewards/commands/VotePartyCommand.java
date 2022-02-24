package me.castiel.voterewards.commands;

import me.castiel.voterewards.VoteRewards;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VotePartyCommand extends BukkitCommand {

    public VotePartyCommand(String name, List<String> aliases) {
        super(name);
        this.description = name;
        this.usageMessage = name;
        this.setAliases(aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        Player player = (Player) sender;
        int votes_required = VoteRewards.getInstance().getConfig().getInt("vote-party.votes-required", 0);
        int current_votes = VoteRewards.getInstance().getDatabaseManager().getMainDatabase().getConfig().getInt("vote-party", 0);
        player.sendMessage(VoteRewards.getInstance().getMessages().votePartyMessage.replace("%vote_party%", String.valueOf(votes_required - current_votes)));
        return true;
    }
}
