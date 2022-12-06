package fr.shykuzo.punishment.utilities.builders;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryBuilder {
	
	private Inventory inventory;
	
		// -------------------- \\
	
	public InventoryBuilder(String inventoryTitle, int inventorySize) {
		inventory = Bukkit.createInventory(null, inventorySize * 9, inventoryTitle);
	}
	
	public InventoryBuilder(Inventory inventory) {
		this.inventory = inventory;
	}
	
		// -------------------- \\
	
	public InventoryBuilder addItem(ItemStack itemStack) {
		inventory.addItem(itemStack);
		
		return this;
	}
	
	public InventoryBuilder setItem(int itemSlot, ItemStack itemStack) {
		inventory.setItem(itemSlot, itemStack);
		
		return this;
	}
	
	public InventoryBuilder remove(ItemStack itemStack) {
		inventory.remove(itemStack);
		
		return this;
	}
	
	public InventoryBuilder clear() {
		inventory.clear();
		
		return this;
	}
	
		// -------------------- \\
	
	public Inventory build() {		
		return inventory;
	}
}
