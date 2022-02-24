package me.castiel.voterewards.util;

import me.castiel.voterewards.VoteRewards;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryGUI implements Listener {

    private final Inventory inventory;
    private final Map<Integer, ItemButton> buttons = new HashMap<>();

    public InventoryGUI(Inventory inventory) {
        this.inventory = inventory;
        Bukkit.getPluginManager().registerEvents(this, VoteRewards.getInstance());
    }


    public Inventory getInventory() {
        return inventory;
    }

    public void addButton(ItemButton button, int slot) {
        button.setSlot(slot);
        inventory.setItem(slot, button.getItem());
        buttons.put(slot, button);
    }

    public void addButton(int slot, ItemButton button) {
        addButton(button, slot);
    }

    public void fill(int start, int end, ItemStack item) {
        for (int i = start; i < end; i++) {
            inventory.setItem(i, item == null ? null : item.clone());
        }
    }

    public void update() {
        for (ItemButton button : buttons.values()) {
            inventory.setItem(button.getSlot(), button.getItem());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!inventory.equals(e.getView().getTopInventory())) {
            return;
        }
        if (e.getAction() == InventoryAction.COLLECT_TO_CURSOR && !e.getClickedInventory().equals(inventory)) {
            e.setCancelled(true);
            return;
        }
        if (!inventory.equals(e.getClickedInventory()) && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            e.setCancelled(true);
        }
        if (e.getInventory().equals(e.getClickedInventory())) {
            e.setCancelled(true);
            ItemButton button = buttons.get(e.getSlot());
            if (button != null) {
                button.onClick(e);
            }
        }
    }
}