package tk.thundaklap.enchantism;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Enchantism extends JavaPlugin implements Listener {
    
    private List<EnchantInventory> inventories = new ArrayList<EnchantInventory>();
    public EnchantismConfiguration configuration;
    private static Enchantism instance;
    
    public void onEnable() {
        
        saveDefaultConfig();
        
        configuration = new EnchantismConfiguration(this);
        getServer().getPluginManager().registerEvents(this, this);
        instance = this;
    }
    
    public void onDisable() {
        // TODO: Place any custom disable code here.
    }
    
    public static Enchantism getInstance(){
        return instance;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryOpen(InventoryOpenEvent event){
        getLogger().info("Inventory Open Event fired.");
        if(event.getInventory() instanceof EnchantingInventory){
            event.setCancelled(true);
            
            inventories.add(new EnchantInventory((Player)event.getPlayer()));
            
        }
    
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent event){
        
        EnchantInventory toRemove = null;
        
        for(EnchantInventory inv : inventories){
            if(inv.player.equals(event.getPlayer())){
                toRemove = inv;
                break;
            }
        }
        
        if(toRemove != null){
            inventories.remove(toRemove);
        }
        
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event){
        
        for(EnchantInventory inv : inventories){
            if(inv.player.equals(event.getWhoClicked())){
                inv.inventoryClicked(event);
                break;
            }
        }
        
    }

}

