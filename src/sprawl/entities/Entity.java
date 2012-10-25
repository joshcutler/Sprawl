package sprawl.entities;

import static org.lwjgl.opengl.GL11.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import sprawl.Constants;
import sprawl.EntityDirection;
import sprawl.LightSource;
import sprawl.PhysicsEngine;
import sprawl.PhysicsType;
import sprawl.RenderingEngine;
import sprawl.Vec2;

public abstract class Entity {
	protected float acceleration;
	protected EntityDirection direction;
	protected int height;
	protected float jumpSpeed;
	
	protected LightSource lightSource;

	protected Vec2 linearVelocity;

	protected float mass = 1;
	protected boolean onSolidground = false;
	protected PhysicsType physicsType;
	protected float speed;
	public Texture texture;

	public String texture_location;

	protected int width;

	protected float x;
	
	protected float y;
	
	public void accelerateX(float x) {
		this.linearVelocity.x += x;
		if (this.linearVelocity.x > this.speed) {
			this.linearVelocity.x = this.speed;
		} else if (this.linearVelocity.x < -(this.speed)) {
			this.linearVelocity.x = -(this.speed);
		}
	}

	public void accelerateY(float y) {
		this.linearVelocity.y += y;
	}
	public void bind() throws Exception {
		this.texture.bind();
	}
	public void changeDirection(EntityDirection dir) {
		this.direction = dir;
		this.loadTexture();
	}
	public void draw() {
		try {
			this.bind();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	protected void loadTexture() {
		try {
			String filename = this.texture_location + "-" + this.direction.toString().toLowerCase() + ".png";
			this.texture = TextureLoader.getTexture("PNG",
					RenderingEngine.class.getResourceAsStream(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
