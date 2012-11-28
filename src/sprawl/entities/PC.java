package sprawl.entities;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import sprawl.Constants;
import sprawl.LightSource;
import sprawl.PhysicsType;
import sprawl.RenderingEngine;
import sprawl.items.Item;
import sprawl.items.ItemType;

public class PC extends Killable{
	private int inventorySize;
	Item[] inventory;
	
	protected Texture headTexture;
	protected String headTextureLocation;
	protected Texture torsoTexture;
	protected String torsoTextureLocation;
	protected Texture legsTexture;
	protected String legsTextureLocation;
	
	public PC() {
		this.height = Constants.BLOCK_SIZE * 4;
		this.width = Constants.BLOCK_SIZE * 2 - Constants.BLOCK_SIZE / 2;
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
		this.inventory = new Item[this.inventorySize];
		this.headTextureLocation = "/textures/pc-head.png";
		this.torsoTextureLocation = "/textures/pc-torso.png";
		this.legsTextureLocation = "/textures/pc-legs.png";
		
		this.loadTexture();
		
		// Initialize some basic items
		this.inventory[0] = new Item(ItemType.STONE_BLOCK);
		this.inventory[0].addToStack(100);
		this.inventory[1] = new Item(ItemType.DIRT_BLOCK);
		this.inventory[1].addToStack(100);
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
		try {
			this.headTexture = TextureLoader.getTexture("PNG",
					RenderingEngine.class.getResourceAsStream(this.headTextureLocation));
			System.out.println("Texture Loaded: " + this.headTextureLocation);
			this.torsoTexture = TextureLoader.getTexture("PNG",
					RenderingEngine.class.getResourceAsStream(this.torsoTextureLocation));
			System.out.println("Texture Loaded: " + this.torsoTextureLocation);
			this.legsTexture = TextureLoader.getTexture("PNG",
					RenderingEngine.class.getResourceAsStream(this.legsTextureLocation));
			System.out.println("Texture Loaded: " + this.legsTextureLocation);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void draw() {
		int bumpDown = 4;
		drawLegs(bumpDown);
		drawTorso(bumpDown);
		drawHead(bumpDown);
	}
	
	private void drawLegs(int bumpDown) {
		int legsHeight = 32, legsWidth = 32, legsSprites = 16;
		int legsY = Math.round(y) + 39 + bumpDown;
		try {
			this.legsTexture.bind();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.direction == EntityDirection.LEFT) {
			int legsX = Math.round(x) - 4;
	    	glBegin(GL_QUADS);
	    		glTexCoord2f(1, 0);
		    	glVertex2f(legsX, legsY);
		    	glTexCoord2f(0, 0);
		    	glVertex2f(legsWidth + legsX, legsY);
		    	glTexCoord2f(0, 1f/legsSprites);
		    	glVertex2f(legsWidth + legsX, legsHeight + legsY);
		    	glTexCoord2f(1, 1f/legsSprites);
		    	glVertex2f(legsX, legsHeight + legsY);
		    glEnd();
		} else if (this.direction == EntityDirection.RIGHT) {
			glBegin(GL_QUADS);
				int legsX = Math.round(x) + 4;
	    		glTexCoord2f(0, 0);
		    	glVertex2f(legsX, legsY);
		    	glTexCoord2f(1, 0);
		    	glVertex2f(legsWidth + legsX, legsY);
		    	glTexCoord2f(1, 1f/legsSprites);
		    	glVertex2f(legsWidth + legsX, legsHeight + legsY);
		    	glTexCoord2f(0, 1f/legsSprites);
		    	glVertex2f(legsX, legsHeight + legsY);
		    glEnd();
		}
	}
	
	private void drawTorso(int bumpDown) {
		int torsoHeight = 32, torsoWidth = 32, torsoSprites = 16;
		int torsoY = Math.round(y) + 15 + bumpDown;
		int torsoX = Math.round(x);
		try {
			this.torsoTexture.bind();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.direction == EntityDirection.LEFT) { 
	    	glBegin(GL_QUADS);
	    		glTexCoord2f(1, 0);
		    	glVertex2f(torsoX, torsoY);
		    	glTexCoord2f(0, 0);
		    	glVertex2f(torsoWidth + torsoX, torsoY);
		    	glTexCoord2f(0, 1f/torsoSprites);
		    	glVertex2f(torsoWidth + torsoX, torsoHeight + torsoY);
		    	glTexCoord2f(1, 1f/torsoSprites);
		    	glVertex2f(torsoX, torsoHeight + torsoY);
		    glEnd();
		} else if (this.direction == EntityDirection.RIGHT) {
			glBegin(GL_QUADS);
	    		glTexCoord2f(0, 0);
		    	glVertex2f(torsoX, torsoY);
		    	glTexCoord2f(1, 0);
		    	glVertex2f(torsoWidth + torsoX, torsoY);
		    	glTexCoord2f(1, 1f/torsoSprites);
		    	glVertex2f(torsoWidth + torsoX, torsoHeight + torsoY);
		    	glTexCoord2f(0, 1f/torsoSprites);
		    	glVertex2f(torsoX, torsoHeight + torsoY);
		    glEnd();
		}
	}
	
	private void drawHead(int bumpDown) {
		int headHeight = 32, headWidth = 32, headSprites = 16;
		int headY = Math.round(y) + bumpDown;
		int headX = Math.round(x);
		try {
			this.headTexture.bind();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.direction == EntityDirection.LEFT) { 
	    	glBegin(GL_QUADS);
	    		glTexCoord2f(1, 0);
		    	glVertex2f(headX, headY);
		    	glTexCoord2f(0, 0);
		    	glVertex2f(headWidth + headX, headY);
		    	glTexCoord2f(0, 1f/headSprites);
		    	glVertex2f(headWidth + headX, headHeight + headY);
		    	glTexCoord2f(1, 1f/headSprites);
		    	glVertex2f(headX, headHeight + headY);
		    glEnd();
		} else if (this.direction == EntityDirection.RIGHT) {
			glBegin(GL_QUADS);
	    		glTexCoord2f(0, 0);
		    	glVertex2f(headX, headY);
		    	glTexCoord2f(1, 0);
		    	glVertex2f(headWidth + headX, headY);
		    	glTexCoord2f(1, 1f/headSprites);
		    	glVertex2f(headWidth + headX, headHeight + headY);
		    	glTexCoord2f(0, 1f/headSprites);
		    	glVertex2f(headX, headHeight + headY);
		    glEnd();
		}
	}
}
