package tk.thundaklap.enchantism;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
        
        for(Player p : openInventories.keySet()){
            p.closeInventory();
        }
        
        openInventories.clear();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args[0].equals("reload") && sender.hasPermission("enchantism.reload")) {
            configuration.reload();
            sender.sendMessage(ChatColor.GOLD + "Enchantism configuration reloaded successfully.");
        }
        return true;
    }

    public static Enchantism getInstance() {
        return instance;
    }
}
