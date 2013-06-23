package tk.thundaklap.enchantism;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemConstants {

    // Clone & Modify
    public static final ItemStack PLAIN_BOOK;
    public static final ItemStack ENCH_BOOK;

    // Just Use
    public static final ItemStack WHITE_WOOL;
    public static final ItemStack BLACK_WOOL;
    public static final ItemStack ENCH_TABLE;

    // Have Meta
    public static final ItemStack UNAVAILABLE;
    public static final ItemStack PREV_PAGE_ITEM;
    public static final ItemStack NEXT_PAGE_ITEM;
    public static final ItemStack UNENCHANT_ITEM;

    static {
        ItemMeta meta;

        PLAIN_BOOK = new ItemStack(Material.BOOK);
        ENCH_BOOK = new ItemStack(Material.ENCHANTED_BOOK);

        WHITE_WOOL = new ItemStack(Material.WOOL, 1, DyeColor.WHITE.getWoolData());
        BLACK_WOOL = new ItemStack(Material.WOOL, 1, DyeColor.BLACK.getWoolData());
        ENCH_TABLE = new ItemStack(Material.ENCHANTMENT_TABLE);

        UNAVAILABLE = new ItemStack(Material.BOOK);
        meta = UNAVAILABLE.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Unavailable");
        UNAVAILABLE.setItemMeta(meta);

        PREV_PAGE_ITEM = new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData());
        meta = PREV_PAGE_ITEM.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Previous Page");
        PREV_PAGE_ITEM.setItemMeta(meta);

        NEXT_PAGE_ITEM = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getWoolData());
        meta = NEXT_PAGE_ITEM.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Next Page");
        NEXT_PAGE_ITEM.setItemMeta(meta);

        UNENCHANT_ITEM = new ItemStack(Material.BLAZE_POWDER);
        meta = UNENCHANT_ITEM.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Remove Enchantments");
        UNENCHANT_ITEM.setItemMeta(meta);


    }
}
