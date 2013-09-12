package tk.thundaklap.enchantism;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Enchantism extends JavaPlugin {

    public EnchantismConfiguration configuration;
    private static Enchantism instance;
    static Map<Player, EnchantInventory> openInventories;

    public void onEnable() {
        
        openInventories = new HashMap<Player, EnchantInventory>();
        
        instance = this;
        saveDefaultConfig();
        configuration = new EnchantismConfiguration();
        getServer().getPluginManager().registerEvents(new EnchantismListener(), this);

    }

    public void onDisable() {
        
        System.out.println(openInventories);
        
        for(Player p : openInventories.keySet()){
            p.closeInventory();
        }
        
        openInventories.clear();
    }

    public static Enchantism getInstance() {
        return instance;
    }
}
