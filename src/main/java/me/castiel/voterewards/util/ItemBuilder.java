package me.castiel.voterewards.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemBuilder extends ItemStack {

    public ItemBuilder(Material material) {
        super(material);
    }

    public ItemBuilder addLore(Iterable<String> lines) {
        ItemUtils.addLore(this, lines);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemUtils.rename(this, name);
        return this;
    }

    public ItemBuilder setAmount_(Integer amount) {
        ItemUtils.setamount(this, amount);
        return this;
    }

    public ItemBuilder setGlowing(boolean glow) {
        if (glow)
            ItemUtils.setGlowingTrue(this);
        return this;
    }
}