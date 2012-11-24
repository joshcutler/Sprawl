package sprawl.items;

import sprawl.RandomString;
import sprawl.world.BlockType;

public class Item {
	ItemType type;
	int quantity = 1;
	private String hash;
	
	public Item(ItemType type) {
		this.type = type;
		this.hash = new RandomString(10).nextString();
	}
	
	public void addToStack(int i) {
		if (this.type.stackable) {
			quantity += i;
		}
	}

	public ItemType getType() {
		return type;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public BlockType placeAs() {
		if (this.type.placeable) {
			if (this.type == ItemType.DIRT_BLOCK) {
				return BlockType.DIRT;
			} else if (this.type == ItemType.STONE_BLOCK) {
				return BlockType.STONE;
			}
		}
		return null;
	}
	
	public boolean hasHash(String toTest) {
		return this.hash.equals(toTest);
	}

	public String getHash() {
		return hash;
	}
	
	public boolean decrement(int count) {
		this.quantity -= count;
		return(this.quantity >= 0);
	}
}
