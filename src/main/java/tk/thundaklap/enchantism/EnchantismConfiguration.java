
package tk.thundaklap.enchantism;

import org.bukkit.enchantments.Enchantment;
import java.util.Map;
import java.util.HashMap;


public class EnchantismConfiguration {

    private Map<Enchantment, Map<Integer, Integer>> cachedCosts = new HashMap<Enchantment, Map<Integer, Integer>>();
    
    public void load(){
        for(Enchantment enchant : Enchantment.values()){
            
            Map<Integer, Integer> levelsForEnchant = new HashMap<Integer, Integer>();
            
            for(int i = 1; i <= 4; i++){
                levelsForEnchant.put(i, Enchantism.getInstance().getConfig().getInt("enchantments." + enchant.getName().toLowerCase().replace('_', '-') + ".level" + i));
            }
            
            cachedCosts.put(enchant, levelsForEnchant);
            
        }
        
    }
    
    public int getCost(Enchantment enchant, int level){
        
        return cachedCosts.get(enchant).get(level);
        
    }
    
}
    

