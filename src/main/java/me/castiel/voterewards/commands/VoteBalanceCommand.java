package me.castiel.voterewards.commands;

import me.castiel.voterewards.VoteRewards;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VoteBalanceCommand extends BukkitCommand {

    public VoteBalanceCommand(String name, List<String> aliases) {
        super(name);
        this.description = name;
        this.usageMessage = name;
        this.setAliases(aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        Player player = (Player) sender;
        int balance = VoteRewards.getInstance().getDatabaseManager().getConfig(player.getUniqueId().toString()).getConfig().getInt("balance");
        player.sendMessage(VoteRewards.getInstance().getMessages().playerBalanceMessage.replace("%bal%", String.valueOf(balance)));
        return true;
    }
}
