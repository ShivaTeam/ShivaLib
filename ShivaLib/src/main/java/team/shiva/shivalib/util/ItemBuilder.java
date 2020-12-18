package team.shiva.shivalib.util;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ItemBuilder {
    private final ItemStack item;

    public ItemBuilder(ItemStack item) {
        this.item=item;
    }

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material , amount);
    }

    public ItemStack build(){
        return item.clone();
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material, 1);
    }

    public static ItemBuilder of(Material material, int amount) {
        return new ItemBuilder(material, Integer.max(amount, 0));
    }

    public static ItemBuilder copyOf(ItemStack item){
        return new ItemBuilder(item);
    }

    public static ItemBuilder copyOf(ItemBuilder builder){
        return new ItemBuilder(builder.build());
    }

    public ItemBuilder amount(int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemBuilder data(short data) {
        this.item.setDurability(data);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder unenchant(Enchantment enchantment) {
        this.item.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder name(String displayName) {
        final ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName((displayName == null) ? null : ChatColor.translateAlternateColorCodes('&', displayName));
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addToLore(String... strings) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(this.item.getType());
        }
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = Lists.newArrayList();
        }
        lore.addAll(Arrays.stream(strings).map(str -> ChatColor.translateAlternateColorCodes('&', str)).collect(Collectors.toList()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addToLore(Collection<String> strings) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(this.item.getType());
        }
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = Lists.newArrayList();
        }
        lore.addAll(strings.stream().map(str -> ChatColor.translateAlternateColorCodes('&', str)).collect(Collectors.toList()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(Collection<String> strings){
        ItemMeta meta = this.item.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(this.item.getType());
        }
        List<String> lore = new ArrayList<>(strings.stream().map(str -> ChatColor.translateAlternateColorCodes('&', str)).collect(Collectors.toList()));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setUnbreakable(final boolean unbreakable) {
        final ItemMeta meta = this.item.getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return this;
    }
}
