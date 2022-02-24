package me.castiel.voterewards.configs;

import me.castiel.voterewards.VoteRewards;
import me.castiel.voterewards.util.InventoryGUI;
import me.castiel.voterewards.util.ItemBuilder;
import me.castiel.voterewards.util.ItemButton;
import me.castiel.voterewards.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Optional;

public class Shop {

    private final InventoryGUI inv;

    public Shop(Configuration inventoryCFG) {
        String title = inventoryCFG.getString("title", " ").replace('&', ChatColor.COLOR_CHAR);
        inv = new InventoryGUI(Bukkit.createInventory(null, inventoryCFG.getInt("size", 54), title));
        for (String key : inventoryCFG.getConfigurationSection("rewards").getKeys(false)) {
            Optional<XMaterial> opt = XMaterial.matchXMaterial(inventoryCFG.getString("rewards." + key + ".material", "DIRT"));
            if (key.equalsIgnoreCase("fill-Background")) {
                opt.ifPresent(mat -> inv.fill(0, getInventory().getSize(), new ItemBuilder(mat.parseMaterial())
                        .setName(inventoryCFG.getString("rewards." + key + ".name"))));
                continue;
            }
            int price = inventoryCFG.getInt("rewards." + key + ".price", -1);
            List<String> lore = inventoryCFG.getStringList("rewards." + key + ".lore");
            lore.replaceAll(s -> s.replace('&', ChatColor.COLOR_CHAR));
            lore.replaceAll(s -> s.replace("%price%", String.valueOf(price)));
            opt.ifPresent(mat -> inv.addButton(inventoryCFG.getInt("rewards." + key + ".slot"), ItemButton.create(new ItemBuilder(mat.parseMaterial())
                    .setName(inventoryCFG.getString("rewards." + key + ".name", " ").replace('&', ChatColor.COLOR_CHAR))
                    .setAmount_(inventoryCFG.getInt("rewards." + key + ".amount", 0))
                    .addLore(lore), event -> {
                if (price <= -1) return;
                Player player = (Player) event.getWhoClicked();
                String UUID = player.getUniqueId().toString();
                VoteRewards plugin = VoteRewards.getInstance();
                FileConfiguration db = plugin.getDatabaseManager().getConfig(UUID).getConfig();
                int balance = db.getInt("balance", 0);
                if (price <= balance) {
                    db.set("balance", balance - price);
                    ConsoleCommandSender sender = plugin.getServer().getConsoleSender();
                    for (String command : inventoryCFG.getStringList("rewards." + key + ".commands")){
                        Bukkit.dispatchCommand(sender, command.replace("%player%", player.getName()));
                    }
                    String message = plugin.getMessages().playerPurchasedItem.replace("%item-name%", title).replace("%item-price%", String.valueOf(price));
                    player.sendMessage(message);
                } else {
                    String message = plugin.getMessages().playerInsufficientFunds.replace("%player-balance%", String.valueOf(balance)).replace("%item-price%", String.valueOf(price));
                    player.sendMessage(message);
                }
                player.closeInventory();
            })));
        }
    }

    public Inventory getInventory() {
        return inv.getInventory();
    }
}
