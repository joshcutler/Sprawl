package sprawl.entities;

import sprawl.Constants;
import sprawl.HUD;
import sprawl.LightSource;
import sprawl.PhysicsType;
import sprawl.items.Item;
import sprawl.world.World;

public class ItemEntity extends Entity {
	private Item item;
	
	public ItemEntity(Item i) {
		this.item = i;
		
		this.height = Constants.BLOCK_SIZE;
		this.width = Constants.BLOCK_SIZE;
		this.direction = EntityDirection.RIGHT;
		this.lightSource = LightSource.Entity;
		this.physicsType = PhysicsType.DYNAMIC;
		this.preventSlide = true;
		
		this.texture_location = i.getType().texture_location;
	}
	
	public void collidedWith(World world, Entity e) {
		if (e.hasInventory()) {
			if (e.addItem(item)) {
				world.removeEntity(this);
				HUD.drawInventory();
			}
		}
	}
}
