
package tk.thundaklap.enchantism;

import org.bukkit.enchantments.Enchantment;
import java.util.Map;
import java.util.HashMap;


public class EnchantismConfiguration {

    private Map<EnchantLevel, Integer> cachedCosts = new HashMap<EnchantLevel, Integer>();
    
    Enchantism plugin;
    
    public EnchantismConfiguration(Enchantism parent){
        plugin = parent;
    }
    
    public int getCost(Enchantment enchant, int level){
        
        //Using Integer type so we can check for null
        Integer cost;
        
        if((cost = cachedCosts.get(new EnchantLevel(enchant, level))) == null){
            try{
                //Example: enchantments.dig-speed.level1
                cost = plugin.getConfig().getInt("enchantments." + enchant.getName().toLowerCase().replace('_', '-') + ".level" + String.valueOf(level));
                
            }catch(Exception e){
                cost = -1;
                
            }
            cachedCosts.put(new EnchantLevel(enchant, level), cost);
        }else{
            System.out.println("Enchantment " + enchant.getName() + " was cached successfully.");
        }
        
        return cost;
        
    }
    
    private class EnchantLevel{
        
        public Enchantment enchant;
        public int level;
        
        public EnchantLevel(Enchantment enchant, int level){
            this.enchant = enchant;
            this.level = level;
        }
        
    }
    
}
    

