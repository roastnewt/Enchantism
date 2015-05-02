package tk.thundaklap.enchantism;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Constants {
    /***********************************
     *  Numbers                        *
     ***********************************/
    // Sizes
    public static final int SIZE_INVENTORY = 54;
    public static final int SIZE_HEADER = 18;
    public static final int SIZE_PAGE = SIZE_INVENTORY - SIZE_HEADER;

    // Header slots
    public static final int SLOT_PREV_PAGE = 0;
    public static final int SLOT_CURRENT_ITEM = 4;
    public static final int SLOT_UNENCHANT = 6;
    public static final int SLOT_NEXT_PAGE = 8;
    public static final int SLOT_ENCH_TABLE = 13;

    // Non-header slots
    public static final int SLOT_VANILLA_UI = 31;
    
    // Other numeric constants
    public static final int ENCHANTMENTS_PER_PAGE = 8;
    public static final int WINDOW_BORDER_RAW_SLOT = -999;

    /***********************************
     *  ItemStacks                     *
     ***********************************/
    // Clone & Modify
    public static final ItemStack ITEM_BOOK;
    public static final ItemStack ITEM_ENCH_BOOK;

    // Just Use
    //public static final ItemStack ITEM_WHITE_WOOL;
    //public static final ItemStack ITEM_BLACK_WOOL;
    public static final ItemStack ITEM_YELLOW_PANE;
    public static final ItemStack ITEM_BLACK_PANE;
    public static final ItemStack ITEM_ENCH_TABLE;

    // Have Meta
    public static final ItemStack ITEM_UNAVAILABLE_ENCHANT;
    public static final ItemStack ITEM_PREV_PAGE;
    public static final ItemStack ITEM_NEXT_PAGE;
    public static final ItemStack ITEM_UNENCHANT;
    public static final ItemStack ITEM_VANILLA_UI;

    // Arrays
    public static final ItemStack[] INV_EMPTY_PAGE = new ItemStack[36];
    private static final ItemStack[] INV_PAGE_TEMPLATE = new ItemStack[36]; // getter
    private static final ItemStack[] INV_TOP_ROW_TEMPLATE = new ItemStack[18]; // getter

    static {
        ItemMeta meta;

        ITEM_BOOK = new ItemStack(Material.BOOKSHELF);
        ITEM_ENCH_BOOK = new ItemStack(Material.BOOKSHELF);
        meta = ITEM_ENCH_BOOK.getItemMeta();
        meta.addEnchant(Enchantment.ARROW_DAMAGE, 0, true);
        ITEM_ENCH_BOOK.setItemMeta(meta);
        ITEM_ENCH_BOOK.removeEnchantment(Enchantment.ARROW_DAMAGE);

        //ITEM_WHITE_WOOL = new ItemStack(Material.WOOL, 1, DyeColor.WHITE.getWoolData());
        //ITEM_BLACK_WOOL = new ItemStack(Material.WOOL, 1, DyeColor.BLACK.getWoolData());
        ITEM_YELLOW_PANE = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.YELLOW.getWoolData());
        ITEM_BLACK_PANE = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getWoolData());
        ITEM_ENCH_TABLE = new ItemStack(Material.ENCHANTMENT_TABLE);

        ITEM_UNAVAILABLE_ENCHANT = new ItemStack(Material.BOOKSHELF);
        meta = ITEM_UNAVAILABLE_ENCHANT.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "Unavailable");
        ITEM_UNAVAILABLE_ENCHANT.setItemMeta(meta);

        ITEM_PREV_PAGE = new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData());
        meta = ITEM_PREV_PAGE.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Previous Page");
        ITEM_PREV_PAGE.setItemMeta(meta);

        ITEM_NEXT_PAGE = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getWoolData());
        meta = ITEM_NEXT_PAGE.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Next Page");
        ITEM_NEXT_PAGE.setItemMeta(meta);

        ITEM_UNENCHANT = new ItemStack(Material.BLAZE_POWDER);
        meta = ITEM_UNENCHANT.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Remove Enchantments");
        ITEM_UNENCHANT.setItemMeta(meta);

        ITEM_VANILLA_UI = new ItemStack(Material.BOOK);
        meta = ITEM_VANILLA_UI.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Show Vanilla Enchanting Window");
        ITEM_VANILLA_UI.setItemMeta(meta);
        
        for (int i = 0; i < Constants.SIZE_INVENTORY - Constants.SIZE_HEADER; i++) {
            if (i % 2 == 1) {
                INV_EMPTY_PAGE[i] = ITEM_YELLOW_PANE;
            } else {
                INV_EMPTY_PAGE[i] = ITEM_BLACK_PANE;
            }
        }

        INV_PAGE_TEMPLATE[4] = ITEM_BLACK_PANE;
        INV_PAGE_TEMPLATE[4+9] = ITEM_YELLOW_PANE;
        INV_PAGE_TEMPLATE[4+9+9] = ITEM_BLACK_PANE;
        INV_PAGE_TEMPLATE[4+9+9+9] = ITEM_YELLOW_PANE;

        for (int i = 0; i < SIZE_HEADER; i++) {
            if (i % 2 == 1) {
                INV_TOP_ROW_TEMPLATE[i] = ITEM_YELLOW_PANE;
            } else {
                INV_TOP_ROW_TEMPLATE[i] = ITEM_BLACK_PANE;
            }
        }
        INV_TOP_ROW_TEMPLATE[SLOT_ENCH_TABLE] = ITEM_ENCH_TABLE;
    }

    public static ItemStack[] getPageTemplate() {
        return Arrays.copyOf(INV_PAGE_TEMPLATE, SIZE_PAGE);
    }

    public static ItemStack[] getTopRowTemplate() {
        return Arrays.copyOf(INV_TOP_ROW_TEMPLATE, SIZE_HEADER);
    }
}
