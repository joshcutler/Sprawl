package sprawl.items;

public class Item {
	ItemType type;
	int quantity = 1;
	
	public Item(ItemType type) {
		this.type = type;
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
}
