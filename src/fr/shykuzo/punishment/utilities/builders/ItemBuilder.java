package fr.shykuzo.punishment.utilities.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
	
	private ItemStack itemStack;
	
		// -------------------- \\
	
	public ItemBuilder(Material material, int amount) {
		itemStack = new ItemStack(material, amount);
	}
	
	public ItemBuilder(ItemStack itemStack) {
		this.itemStack = itemStack;
	}
	
		// -------------------- \\
	
	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		itemStack.addUnsafeEnchantment(enchantment, level);
		
		return this;
	}
	
	public ItemBuilder removeEnchantment(Enchantment enchantment) {
		itemStack.removeEnchantment(enchantment);
		
		return this;
	}
	
	public ItemBuilder addItemFlag(ItemFlag itemFlag) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		itemMeta.addItemFlags(itemFlag);
		itemStack.setItemMeta(itemMeta);
		
		return this;
	}
	
	public ItemBuilder removeItemFlag(ItemFlag itemFlag) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		itemMeta.removeItemFlags(itemFlag);
		itemStack.setItemMeta(itemMeta);
		
		return this;
	}
	
	public ItemBuilder setDisplayName(String displayName) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		itemMeta.setDisplayName(displayName);
		itemStack.setItemMeta(itemMeta);
		
		return this;
	}
	
	public ItemBuilder setLore(String... lore) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		itemMeta.setLore(Arrays.asList(lore));
		itemStack.setItemMeta(itemMeta);
		
		return this;
	}
	
	public ItemBuilder setLore(List<String> lore) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		
		return this;
	}
	
	public ItemBuilder removeLoreLine(String loreLine) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		List<String> lore = new ArrayList<>(itemMeta.getLore());
		if(!lore.contains(loreLine)) return this;
		lore.remove(loreLine);
		
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		
		return this;
	}
	
	public ItemBuilder removeLoreLine(int index) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		List<String> lore = new ArrayList<>(itemMeta.getLore());
		if(index < 0 || index > lore.size()) return this;
		lore.remove(index);
		
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		
		return this;
	}
	
	public ItemBuilder setUnbreakable(boolean unbreakable) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		
		itemMeta.setUnbreakable(unbreakable);
		itemStack.setItemMeta(itemMeta);
		
		return this;
	}
	
	public ItemBuilder setSkullOwner(Player player) {
		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
		
		skullMeta.setOwnerProfile(player.getPlayerProfile());
		itemStack.setItemMeta(skullMeta);
		
		return this;
	}
	
		// -------------------- \\
	
	public ItemStack build() {
		return itemStack;
	}
	
}
