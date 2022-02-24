package me.castiel.voterewards.commands;

import me.castiel.voterewards.VoteRewards;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandsManager implements CommandExecutor, TabCompleter {

    private final List<String> voteCommands = new ArrayList<>();
    private List<String> voteBalanceSubCommands = new ArrayList<>();
    private List<String> voteShopSubCommands = new ArrayList<>();
    private List<String> votePartySubCommands = new ArrayList<>();
    private VoteBalanceCommand voteBalanceCommand;
    private VoteShopCommand voteShopCommand;
    private VotePartyCommand votePartyCommand;

    public CommandsManager(FileConfiguration commandsCFG) {
        voteCommands.add("vote");
        voteCommands.add("votes");
        voteCommands.add("voting");
        VoteRewards.getInstance().getCommand("vote").setExecutor(this);
        VoteRewards.getInstance().getCommand("vote").setTabCompleter(this);
        final Field bukkitCommandMap;
        try {
            bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }
        bukkitCommandMap.setAccessible(true);
        CommandMap commandMap;
        try {
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        if (commandsCFG.getBoolean("voteBalanceCommand.enabled", false)) {
            voteBalanceSubCommands = commandsCFG.getStringList("voteBalanceCommand.sub-commands");
            voteBalanceCommand = new VoteBalanceCommand("votebalance", commandsCFG.getStringList("voteBalanceCommand.aliases"));
            commandMap.register("votebalance", voteBalanceCommand);
        }
        if (commandsCFG.getBoolean("voteShopCommand.enabled", false)) {
            voteShopSubCommands = commandsCFG.getStringList("voteShopCommand.sub-commands");
            voteShopCommand = new VoteShopCommand("voteshop", commandsCFG.getStringList("voteShopCommand.aliases"));
            commandMap.register("voteshop", voteShopCommand);
        }
        if (commandsCFG.getBoolean("votePartyCommand.enabled", false)) {
            votePartySubCommands = commandsCFG.getStringList("votePartyCommand.sub-commands");
            votePartyCommand = new VotePartyCommand("voteparty", commandsCFG.getStringList("votePartyCommand.aliases"));
            commandMap.register("voteparty", votePartyCommand);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("vote")) {
            Player player = (Player) sender;
            if (args.length > 0) {
                if (voteBalanceSubCommands.contains(args[0]))
                    voteBalanceCommand.execute(sender, label, args);
                else if (voteShopSubCommands.contains(args[0]))
                    voteShopCommand.execute(sender, label, args);
                else if (votePartySubCommands.contains(args[0]))
                    votePartyCommand.execute(sender, label, args);
                else {
                    for (String message : VoteRewards.getInstance().getMessages().voteHelpMessage) {
                        player.sendMessage(message);
                    }
                }
                return true;
            }
            for (String message : VoteRewards.getInstance().getMessages().voteLinksMessage) {
                player.sendMessage(message);
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        if (!voteBalanceSubCommands.isEmpty())
            completions.add(voteBalanceSubCommands.get(0));
        if (!voteShopSubCommands.isEmpty())
            completions.add(voteShopSubCommands.get(0));
        if (!votePartySubCommands.isEmpty())
            completions.add(votePartySubCommands.get(0));
        StringUtil.copyPartialMatches(args[0], Collections.singletonList(voteCommands.get(0)), completions);
        Collections.sort(completions);
        return completions;
    }
}
