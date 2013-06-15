
package tk.thundaklap.enchantism;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class EnchantPage {

    private ItemStack[] inventory = new ItemStack[36];
    private int enchantIndex = 0;
    
    private Map<Integer, EnchantLevelCost> levelsForSlot = new HashMap<Integer, EnchantLevelCost>();
    
    public EnchantPage(){
        //Center strip
        for(int i = 4; i < 36; i += 9){
            inventory[i] = new ItemStack(Material.WOOL, 1, (byte)15);
        }
    }
    
    public void setEmpty(){
        for(int i = 0; i < inventory.length; i++){
            inventory[i] = new ItemStack(Material.WOOL, 1, (byte)15);
        }
    }
    
    
    public boolean addEnchantment(Enchantment enchant){
        
        // Enchant index too high, this page is full.
        if(enchantIndex >= 36){
            return false;
        }
        
        
        System.arraycopy(getBooksForEnchant(enchant), 0, inventory, enchantIndex, 4);
        
        for(int i = 0; i < 4; i++){
            levelsForSlot.put(enchantIndex + i, new EnchantLevelCost(enchant, i + 1));
        }
        
        enchantIndex += enchantIndex % 9 == 0 ? 5 : 4;
        
        return true;
        
    }
    
    public ItemStack[] getInventory(){
        return inventory;
    }
    
    public EnchantLevelCost enchantAtSlot(int slot){
        return levelsForSlot.get(slot);
    }
    
    public void fill(){
        while(enchantIndex < 36){
            for(int i = 0; i < 4; i++){
                inventory[i + enchantIndex] = new ItemStack(Material.BOOK, 1);
            }
            
            enchantIndex += enchantIndex % 9 == 0 ? 5 : 4;
        }
    }
    
    
    private static ItemStack[] getBooksForEnchant(Enchantment enchant){
       
       ItemStack[] is = new ItemStack[4];
       
       String name = readableNameForEnchantment(enchant);
       
       for(int i = 0; i < 4; i++){
           is[i] = fancyBook(new EnchantLevelCost(enchant, i + 1), name);
       }
       
       return is;
       
   }
  
   
    private static ItemStack fancyBook(EnchantLevelCost enchant,  String name){
 
       ItemStack is;
       
       int cost = enchant.cost;
       
       if(cost > -1){
           is = new ItemStack(Material.ENCHANTED_BOOK, 1);
           
           ItemMeta meta = is.getItemMeta();
           meta.setDisplayName(ChatColor.YELLOW + name + " " + intToRomanNumerals(enchant.level));
           
           List<String> lore = new ArrayList<String>();
           lore.add(ChatColor.ITALIC + "Cost: " + cost + "XP");
           meta.setLore(lore);
           
           is.setItemMeta(meta);
           
       }else{
           
           is = noEnchantment();
       }
       
       return is;
       
   }
    
    private static ItemStack noEnchantment(){
        ItemStack is = new ItemStack(Material.BOOK, 1);
        
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.ITALIC + "" + ChatColor.GRAY + "Not applicable");
                
        return is;
    }
   
    private static String intToRomanNumerals(int i){
       
       switch(i){
           case 1:
               return "I";
           case 2:
               return "II";
           case 3:
               return "III";
           case 4:
               return "IV";
           default:
               return "(ERROR)";
       }
       
       
   }
   
    private static String readableNameForEnchantment(Enchantment enchant){
       
       //Not too proud of this but it will have to do until I think of a better solution
       if(enchant.equals(Enchantment.ARROW_DAMAGE)){
           return "Power";
       }
       if(enchant.equals(Enchantment.ARROW_FIRE)){
           return "Flame";
       }
       if(enchant.equals(Enchantment.ARROW_INFINITE)){
           return "Infinity";
       }
       if(enchant.equals(Enchantment.ARROW_KNOCKBACK)){
           return "Punch";
       }
       if(enchant.equals(Enchantment.DAMAGE_ALL)){
           return "Sharpness";
       }
       if(enchant.equals(Enchantment.DAMAGE_ARTHROPODS)){
           return "Bane of Arthropods";
       }
       if(enchant.equals(Enchantment.DAMAGE_UNDEAD)){
           return "Smite";
       }
       if(enchant.equals(Enchantment.DIG_SPEED)){
           return "Efficiency";
       }
       if(enchant.equals(Enchantment.DURABILITY)){
           return "Unbreaking";
       }
       if(enchant.equals(Enchantment.FIRE_ASPECT)){
           return "Fire Aspect";
       }
       if(enchant.equals(Enchantment.KNOCKBACK)){
           return "Knockback";
       }
       if(enchant.equals(Enchantment.LOOT_BONUS_BLOCKS)){
           return "Fortune";
       }
       if(enchant.equals(Enchantment.LOOT_BONUS_MOBS)){
           return "Looting";
       }
       if(enchant.equals(Enchantment.OXYGEN)){
           return "Respiration";
       }
       if(enchant.equals(Enchantment.PROTECTION_ENVIRONMENTAL)){
           return "Protection";
       }
       if(enchant.equals(Enchantment.PROTECTION_EXPLOSIONS)){
           return "Blast Protection";
       }
       if(enchant.equals(Enchantment.PROTECTION_FALL)){
           return "Feather Falling";
       }
       if(enchant.equals(Enchantment.PROTECTION_FIRE)){
           return "Fire Protection";
       }
       if(enchant.equals(Enchantment.PROTECTION_PROJECTILE)){
           return "Projectile Protection";
       }if(enchant.equals(Enchantment.SILK_TOUCH)){
           return "Silk Touch";
       }if(enchant.equals(Enchantment.THORNS)){
           return "Thorns";
       }if(enchant.equals(Enchantment.WATER_WORKER)){
           return "Aqua Affinity";
       }
       
       return "Undefined";
   }
    
}
