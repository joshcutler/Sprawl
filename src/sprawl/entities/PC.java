package sprawl.entities;

import sprawl.Animation;
import sprawl.Constants;
import sprawl.LightSource;
import sprawl.PhysicsType;
import sprawl.items.Item;
import sprawl.items.ItemType;
import sprawl.world.World;

public class PC extends Killable{
	protected Animation headAnimation;
	protected Animation torsoAnimation;
	protected Animation legsAnimation;
	protected PCLegsState legsState;
	protected PCArmsState armsState;
	protected PCLegsState baseLegsState;
	protected PCArmsState baseArmsState;
	
	protected int digStrength = 1;
	protected int chopStrength = 1;
	
	public PC() {
		this.height = Constants.BLOCK_SIZE * 4;
		this.width = Constants.BLOCK_SIZE * 2 - Constants.BLOCK_SIZE / 2;
		this.acceleration = 2f;
		this.speed = 12f;
		this.walkSpeed = 4f;
		this.jumpSpeed = 25f;
		this.direction = EntityDirection.RIGHT;
		this.lightSource = LightSource.Entity;
		this.physicsType = PhysicsType.DYNAMIC_COLLISION;

		this.maxHealth = 10;
		this.health = 10;
		this.inventorySize = 32;
		this.inventory = new Item[this.inventorySize];
		
		this.headAnimation = new Animation("/textures/pc-head.png", 32, 32, 0, 0, 1f);
		this.torsoAnimation = new Animation("/textures/pc-torso.png", 32, 32, 0, 0, 1f);
		this.legsAnimation = new Animation("/textures/pc-legs.png", 32, 32, 0, 0, 1f);
		
		this.loadTexture();
		
		// Initialize some basic items
		this.inventory[0] = new Item(ItemType.STONE_BLOCK);
		this.inventory[0].addToStack(100);
		this.inventory[1] = new Item(ItemType.DIRT_BLOCK);
		this.inventory[1].addToStack(100);
		
		setLegsState(PCLegsState.STANDING);
		setArmsState(PCArmsState.STANDING);
		this.baseLegsState = PCLegsState.STANDING;
		this.baseArmsState = PCArmsState.STANDING;
	}

	public int getInventorySize() {
		return inventorySize;
	}

	public Item[] getInventory() {
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
			this.removeItem(itemHash);
		} else {
			item.decrement(1);
		}
		return item;
	}
	
	public boolean removeItem(String itemHash) {
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] != null && inventory[i].getHash().equals(itemHash)) {
				inventory[i] = null;
				return true;
			}
		}
		return false;
	}
	
	public void setItemAt(int position, Item item) {
		this.inventory[position] = item;
	}
	
	protected void loadTexture() {
		this.headAnimation.init();
		this.torsoAnimation.init();
		this.legsAnimation.init();
	}
	
	public void draw(int delta) {
		int bumpDown = 4;
		int dX = Math.round(x);
		int dY = Math.round(y);
		boolean flipped = (this.direction == EntityDirection.LEFT);
		legsAnimation.draw(dX, dY + bumpDown + 39, flipped, delta);
		headAnimation.draw(dX, dY + bumpDown, flipped, delta);
		
		if (torsoAnimation.draw(dX, dY + bumpDown + 15, flipped, delta) && armsState.single) {
			this.setArmsState(baseArmsState);
		}
	}
	
	public void setLegsState(PCLegsState newState) {
		if (newState != legsState) {
			this.legsState = newState;
			this.legsAnimation.setFrames(newState.startFrame, newState.stopFrame, newState.duration);
		}
	}
	
	public void setArmsState(PCArmsState newState) {
		if (newState != armsState) {
			this.armsState = newState;
			this.torsoAnimation.setFrames(newState.startFrame, newState.stopFrame, newState.duration);
		}
	}

	public int getDigStrength() {
		return digStrength;
	}
	
	public int getChopStrength() {
		return chopStrength;
	}
	
	public boolean hasInventory() {
		return true;
	}

	@Override
	public void collidedWith(World world, Entity e) {
		// TODO Auto-generated method stub
		
	}
	
	public void jump() {
		super.jump();
		setLegsState(PCLegsState.STANDING);
	}
}
