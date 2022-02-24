package me.castiel.voterewards.commands;

import me.castiel.voterewards.VoteRewards;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VoteShopCommand extends BukkitCommand {

    public VoteShopCommand(String name, List<String> aliases) {
        super(name);
        this.description = name;
        this.usageMessage = name;
        this.setAliases(aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        ((Player) sender).openInventory(VoteRewards.getInstance().getVoteShop());
        return true;
    }
}
