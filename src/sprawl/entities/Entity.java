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
import sprawl.PhysicsEngine;
import sprawl.PhysicsType;
import sprawl.RenderingEngine;
import sprawl.Vec2;
import sprawl.items.Item;
import sprawl.world.World;

public abstract class Entity {
	protected int inventorySize;
	protected Item[] inventory;
	
	protected float acceleration;
	protected EntityDirection direction = EntityDirection.RIGHT;
	protected float jumpSpeed;
	
	protected LightSource lightSource;
	protected Vec2 linearVelocity;

	protected float mass = 1;
	protected boolean onSolidground = false;
	protected boolean facingWall = false;
	protected PhysicsType physicsType;
	protected float speed;
	protected float walkSpeed;
	public boolean preventSlide = false;
	
	public String texture_location;

	protected int width;
	protected int height;
	protected float x;
	protected float y;
	
	public void accelerateX(float x, boolean isWalking) {
		this.linearVelocity.x += x;
		float topSpeed = isWalking ? this.walkSpeed : this.speed;
		if (this.linearVelocity.x > topSpeed) {
			this.linearVelocity.x = topSpeed;
		} else if (this.linearVelocity.x < -(topSpeed)) {
			this.linearVelocity.x = -(topSpeed);
		}
	}

	public void accelerateY(float y) {
		this.linearVelocity.y += y;
	}
	public void bind() throws Exception {
		RenderingEngine.getTexture(this.texture_location).bind();
	}
	
	public void changeDirection(EntityDirection dir) {
		if (dir != this.direction) {
			this.direction = dir;
		}
	}
	
	public void draw(int delta) {
		try {
			this.bind();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.direction == EntityDirection.LEFT) { 
	    	glBegin(GL_QUADS);
	    		glTexCoord2f(0, 0);
		    	glVertex2f(x, y);
		    	glTexCoord2f(1, 0);
		    	glVertex2f(width + x, y);
		    	glTexCoord2f(1, 1);
		    	glVertex2f(width + x, height + y);
		    	glTexCoord2f(0, 1);
		    	glVertex2f(x, height + y);
		    glEnd();
		} else if (this.direction == EntityDirection.RIGHT) {
			glBegin(GL_QUADS);
	    		glTexCoord2f(1, 0);
		    	glVertex2f(x, y);
		    	glTexCoord2f(0, 0);
		    	glVertex2f(width + x, y);
		    	glTexCoord2f(0, 1);
		    	glVertex2f(width + x, height + y);
		    	glTexCoord2f(1, 1);
		    	glVertex2f(x, height + y);
		    glEnd();
		}
	}
	public int getHeight() {
		return height;
	}
	
	public float getJumpSpeed() {
		return jumpSpeed;
	}
	
	public LightSource getLightSource() {
		return lightSource;
	}
	
	public float getLightSourceRadius() {
		if (lightSource != null) {
			return lightSource.distance * Constants.BLOCK_SIZE;
		}
		return 0; 
	}
    public float getLightSourceX() {
		return x + width / 2;
	}
	
	public float getLightSourceY() {
		return y + height / 2;
	}
	
	public Vec2 getLinearVelocity() {
		return linearVelocity;
	}
	
	public float getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	public EntityDirection getDirection() {
		return direction;
	}

	public void setDirection(EntityDirection direction) {
		this.direction = direction;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public PhysicsType getPhysicsType() {
		return physicsType;
	}

	public void setPhysicsType(PhysicsType physicsType) {
		this.physicsType = physicsType;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getWidth() {
		return width;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public int heightBlocksSpanned() {
		return PhysicsEngine.numberOfBlocks((int)Math.ceil(this.y + this.height)) - PhysicsEngine.numberOfBlocks((int)Math.ceil(this.y));
	}

	public int heightInBlocks() {
		return (int) Math.ceil((float)this.height / Constants.BLOCK_SIZE);
	}
	
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean onSolidGround() {
		return onSolidground;
	}
	
	public void onSolidGround(boolean b) {
		onSolidground = b;
	}
		
	public void setHeight(int height) {
		this.height = height;
	}

	public void setJumpSpeed(float jump_speed) {
		this.jumpSpeed = jump_speed;
	}
	
	public void setLinearVelocity(Vec2 linearVelocity) {
		this.linearVelocity = linearVelocity;
	}
	
	public void setVelocityX(float x) {
		this.linearVelocity.x = x;
	}
	
	public void setVelocityY(float y) {
		this.linearVelocity.y = y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}

	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public int widthBlocksSpanned() {
 		return PhysicsEngine.numberOfBlocks((int)Math.floor(this.x + this.width)) - PhysicsEngine.numberOfBlocks((int) Math.ceil(this.x)) + 1;
	}
	
	public int widthInBlocks() {
		return (int) Math.ceil((float)this.width / Constants.BLOCK_SIZE);
	}
	
	public abstract void collidedWith(World world, Entity e);
	
	public void jump() {
		if (onSolidground) {
			setVelocityY(jumpSpeed);
		}		
	}
	
	public boolean hasInventory() {
		return false;
	}
	
	public boolean isMOB() {
		return false;
	}
	
	public boolean addItem(Item item) {
		for (int i = 0; i < inventorySize; i++) {
			Item cItem = inventory[i];
			if (cItem != null && cItem.getType() == item.getType() && cItem.getType().stackable) {
				cItem.addToStack(item.getQuantity());
				return true;
			} else if (cItem == null) {
				inventory[i] = item;
				return true;
			}
		}
		return false;
	}
	
	public boolean getFacingWall() {
		return facingWall;
	}
	
	public void setFacingWall(boolean fw) {
		this.facingWall = fw;
	}
}
