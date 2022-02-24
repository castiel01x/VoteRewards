package me.castiel.voterewards.util;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;

import java.util.function.BiConsumer;

public abstract class ItemButton {

    protected ItemStack item;
    private int slot;

    public static ItemButton create(ItemStack item, Consumer<InventoryClickEvent> listener) {
        return new ItemButton(item) {

            @Override
            public void onClick(InventoryClickEvent e) {
                listener.accept(e);
            }

        };
    }

    public static ItemButton create(ItemStack item, BiConsumer<InventoryClickEvent, ItemButton> listener) {
        return new ItemButton(item) {

            @Override
            public void onClick(InventoryClickEvent e) {
                listener.accept(e, this);
            }

        };
    }

    public ItemButton(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    protected int getSlot() {
        return slot;
    }

    protected void setSlot(int slot) {
        this.slot = slot;
    }

    public abstract void onClick(InventoryClickEvent e);

}
