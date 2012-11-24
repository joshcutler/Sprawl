package sprawl.entities;

import java.util.ArrayList;

import sprawl.Constants;
import sprawl.LightSource;
import sprawl.PhysicsType;
import sprawl.items.Item;
import sprawl.items.ItemType;

public class PC extends Killable{
	private int inventorySize;
	ArrayList<Item> inventory = new ArrayList<Item>();
	
	public PC() {
		this.height = Constants.BLOCK_SIZE * 4;
		this.width = Constants.BLOCK_SIZE * 2 - Constants.BLOCK_SIZE / 2;
		this.texture_location = "/textures/PC.png";
		this.acceleration = 2f;
		this.speed = 12f;
		this.walkSpeed = 4f;
		this.jumpSpeed = 25f;
		this.direction = EntityDirection.RIGHT;
		this.lightSource = LightSource.Entity;
		this.physicsType = PhysicsType.DYNAMIC;
		this.maxHealth = 10;
		this.health = 10;
		this.inventorySize = 32;
		
		this.loadTexture();
		
		// Initialize some basic items
		this.inventory.add(new Item(ItemType.STONE_BLOCK));
		((Item) this.inventory.get(0)).addToStack(100);
		this.inventory.add(new Item(ItemType.DIRT_BLOCK));
		((Item) this.inventory.get(1)).addToStack(100);
	}

	public int getInventorySize() {
		return inventorySize;
	}

	public ArrayList<Item> getInventory() {
		return inventory;
	}
	
	public Item getItemByHash(String itemHash) {
		for (Item i : inventory) {
			if (i != null) {
				if (i.hasHash(itemHash)) {
					return i;
				}
			}
		}
		return null;
	}
	
	public Item removeItemInstance(String itemHash) {
		Item item = this.getItemByHash(itemHash);
		
		if (item.getQuantity() == 1) {
			this.inventory.remove(item);
		} else {
			item.decrement(1);
		}
		return item;
	}
}
