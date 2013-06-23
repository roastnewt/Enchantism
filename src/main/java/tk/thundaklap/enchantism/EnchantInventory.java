package tk.thundaklap.enchantism;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import static tk.thundaklap.enchantism.ItemConstants.*;

public final class EnchantInventory {

    public Player player;
    private EnchantPage[] pages;
    private int pageCount = 0;
    private int currentPage = 0;
    private Inventory inventory;
    private boolean showUnenchant = false;
    private boolean unenchantEnabled;

    // Slot constants
    public static final int CURRENT_ITEM_SLOT = 4;
    public static final int INVENTORY_SIZE = 54;
    public static final int PREV_PAGE_SLOT = 0;
    public static final int NEXT_PAGE_SLOT = 8;
    public static final int UNENCHANT_SLOT = 6;
    public static final int ENCHANTMENT_TABLE_SLOT = 13;
    public static final int THIRD_ROW_START = 18;

    // Other constants
    public static final int ENCHANTMENTS_PER_PAGE = 8;

    public EnchantInventory(Player player) {
        unenchantEnabled = Enchantism.getInstance().configuration.enableUnenchantButton;
        this.player = player;
        this.inventory = Bukkit.createInventory(player, INVENTORY_SIZE, "Enchant an Item");
        slotChange();
        this.player.openInventory(inventory);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void updatePlayerInv() {
        boolean isMultiPage = pageCount != 0;
        inventory.setContents((ItemStack[]) ArrayUtils.addAll(topRows(isMultiPage && pageCount != currentPage, isMultiPage && currentPage != 0, showUnenchant && unenchantEnabled), pages[currentPage].getInventory()));
        new DelayUpdateInventory(player).runTask(Enchantism.getInstance());
    }

    public void slotChange() {
        ItemStack change = inventory.getItem(CURRENT_ITEM_SLOT);
        List<Enchantment> applicableEnchantments = Utils.getEnchantments(change);

        currentPage = 0;

        if (applicableEnchantments.isEmpty()) {
            pageCount = 0;
            pages = new EnchantPage[1];
            pages[0] = new EnchantPage();
            pages[0].setEmpty();

            // allow unenchanting of books
            showUnenchant = change == null ? false : change.getType() == Material.ENCHANTED_BOOK;

        } else {
            int numberOfEnchants = applicableEnchantments.size();
            pageCount = (numberOfEnchants - 1) / ENCHANTMENTS_PER_PAGE;
            pages = new EnchantPage[pageCount + 1];

            for (int i = 0; i < pages.length; i++) {
                pages[i] = new EnchantPage();
            }

            int currentlyAddingPage = 0;

            for (Enchantment enchant : applicableEnchantments) {
                // Method returns false if the page is full.
                if (!pages[currentlyAddingPage].addEnchantment(enchant)) {
                    pages[++currentlyAddingPage].addEnchantment(enchant);
                }
            }

            pages[currentlyAddingPage].fill();
            showUnenchant = true;
        }
        updatePlayerInv();
    }

    public void inventoryClicked(InventoryClickEvent event) {
        int rawSlot = event.getRawSlot();
        InventoryView view = event.getView();
        assert INVENTORY_SIZE == view.getTopInventory().getSize();

        // Default to cancel, uncancel if we want vanilla behavior
        event.setResult(Result.DENY);

        BukkitTask task = Bukkit.getScheduler().runTask(Enchantism.getInstance(), new SlotChangeTask(this, inventory.getItem(CURRENT_ITEM_SLOT)));

        // Let people shift-click in tools
        if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
            if (rawSlot >= INVENTORY_SIZE) {
                // Swappy swap swap!
                ItemStack old = view.getItem(CURRENT_ITEM_SLOT);
                view.setItem(CURRENT_ITEM_SLOT, view.getItem(rawSlot));
                view.setItem(rawSlot, old);
                //slotChange();
                //task.cancel();
                return;
            }
        }

        // Predefined slot behavior
        if (rawSlot == PREV_PAGE_SLOT) {
            if (currentPage != 0 && pageCount > 0) {
                currentPage--;
                updatePlayerInv();
                player.playSound(player.getLocation(), Sound.CLICK, 2F, 1F);
            }
            return;

        } else if (rawSlot == NEXT_PAGE_SLOT) {
            if (currentPage != pageCount) {
                currentPage++;
                updatePlayerInv();
                player.playSound(player.getLocation(), Sound.CLICK, 2F, 1F);
            }
            return;

        } else if (rawSlot == CURRENT_ITEM_SLOT) {
            event.setResult(Result.DEFAULT);
            return;

        } else if (rawSlot == UNENCHANT_SLOT) {
            if (showUnenchant && unenchantEnabled && event.getClick() == ClickType.LEFT) {
                ItemStack item = inventory.getItem(CURRENT_ITEM_SLOT);

                if (item != null && !item.getType().equals(Material.AIR)) {
                    if (item.getType() == Material.ENCHANTED_BOOK) {
                        /*EnchantmentStorageMeta meta = (EnchantmentStorageMeta)item.getItemMeta();
                        for (Map.Entry<Enchantment, Integer> entry : meta.getStoredEnchants().entrySet()) {
                            meta.removeStoredEnchant(entry.getKey());
                        }*/
                        item.setType(Material.BOOK);
                    } else {
                        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
                            item.removeEnchantment(entry.getKey());
                        }
                    }
                    player.playSound(player.getLocation(), Sound.GLASS, 2F, 1F);
                    slotChange();
                }
            }
            return;

        } else if (rawSlot >= THIRD_ROW_START && rawSlot < INVENTORY_SIZE) {
            EnchantLevelCost enchant = pages[currentPage].enchantAtSlot(rawSlot - THIRD_ROW_START);

            if (enchant == null) {
                return;
            }

            if (player.getLevel() < enchant.cost) {
                player.sendMessage(ChatColor.RED + "You don\'t have enough XP to enchant the item with that enchantment!");
                player.playSound(player.getLocation(), Sound.ANVIL_BREAK, 2F, 1F);
                return;
            }

            player.setLevel(player.getLevel() - enchant.cost);
            player.playSound(player.getLocation(), Sound.NOTE_SNARE_DRUM, 2F, 1F);

            ItemStack item = inventory.getItem(CURRENT_ITEM_SLOT);

            if (item.getType() == Material.BOOK) {
                item.setType(Material.ENCHANTED_BOOK);

                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                meta.addStoredEnchant(enchant.enchant, enchant.level, true);

                item.setItemMeta(meta);
                return;
            }

            try {
                item.addUnsafeEnchantment(enchant.enchant, enchant.level);
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "[Enchantism] Unexpected error. See console for details.");
                Enchantism.getInstance().getLogger().severe(e.getMessage());
            }
            inventory.setItem(CURRENT_ITEM_SLOT, item);
        } else if (rawSlot >= INVENTORY_SIZE) {
            // Uncancel, unless on blacklist
            if (event.getAction() != InventoryAction.COLLECT_TO_CURSOR && event.getAction() != InventoryAction.CLONE_STACK && event.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                event.setResult(Result.DEFAULT);
            }
        }
    }

    public void inventoryDragged(InventoryDragEvent event) {
        if (event.getRawSlots().contains(CURRENT_ITEM_SLOT)) {
            Bukkit.getScheduler().runTask(Enchantism.getInstance(), new SlotChangeTask(this, inventory.getItem(CURRENT_ITEM_SLOT)));
        }
    }

    private ItemStack[] topRows(boolean showNextPage, boolean showPrevPage, boolean showUnenchantButton) {

        ItemStack[] is = new ItemStack[THIRD_ROW_START];

        for (int i = 0; i < 18; i++) {

            switch (i) {
            case PREV_PAGE_SLOT:
                if (showPrevPage) {
                    is[i] = PREV_PAGE_ITEM;
                } else {
                    is[i] = WHITE_WOOL;
                }
                break;

            case CURRENT_ITEM_SLOT:
                is[i] = inventory.getItem(CURRENT_ITEM_SLOT);
                break;

            case UNENCHANT_SLOT:
                if (showUnenchantButton) {
                    is[i] = UNENCHANT_ITEM;
                } else {
                    is[i] = WHITE_WOOL;
                }
                break;

            case NEXT_PAGE_SLOT:
                if (showNextPage) {
                    is[i] = NEXT_PAGE_ITEM;
                } else {
                    is[i] = WHITE_WOOL;
                }
                break;

            case ENCHANTMENT_TABLE_SLOT:
                is[i] = ENCH_TABLE;
                break;
            default:
                is[i] = WHITE_WOOL;
            }
        }
        return is;
    }
}
