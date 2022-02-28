package me.castiel.voterewards.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static ItemStack rename(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addLore(ItemStack item, Iterable<String> lines) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore = lore == null ? new ArrayList<>() : lore;
        lines.forEach(lore::add);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setamount(ItemStack item, Integer amount) {
        item.setAmount(amount);
        return item;
    }

    public static ItemStack setGlowingTrue(ItemStack item) {
        item.addEnchantment(Enchantment.DURABILITY, 1);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }
}