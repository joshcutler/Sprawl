package sprawl;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.newdawn.slick.opengl.Texture;

public abstract class Entity {
	protected PhysicsType physicsType = PhysicsType.DYNAMIC;
	protected Vec2 linearVelocity;
	
	public Vec2 getLinearVelocity() {
		return linearVelocity;
	}

	public void setLinearVelocity(Vec2 linearVelocity) {
		this.linearVelocity = linearVelocity;
	}

	protected float x;
	protected float y;
	protected int height;
	protected int width;
	protected float jumpSpeed;
	protected float speed;
	protected float mass = 1;
	protected boolean onSolidground = false;
	
	public boolean onSolidGround() {
		return onSolidground;
	}
	
	public void onSolidGround(boolean b) {
		onSolidground = b;
	}
	
	public String texture_location;
    public Texture texture;
	
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
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
	
	public void bind() throws Exception {
		this.texture.bind();
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public int heightInBlocks() {
		return (int) Math.ceil((float)this.height / Constants.BLOCK_SIZE);
	}
	
	public int widthInBlocks() {
		return (int) Math.ceil((float)this.width / Constants.BLOCK_SIZE);
	}
	
	public int widthBlocksSpanned() {
 		return PhysicsEngine.numberOfBlocks((int)Math.floor(this.x + this.width)) - PhysicsEngine.numberOfBlocks((int) Math.ceil(this.x)) + 1;
	}
	
	public int heightBlocksSpanned() {
		return PhysicsEngine.numberOfBlocks((int)Math.ceil(this.y + this.height)) - PhysicsEngine.numberOfBlocks((int)Math.ceil(this.y));
	}
		
	public float getJumpSpeed() {
		return jumpSpeed;
	}

	public void setJumpSpeed(float jump_speed) {
		this.jumpSpeed = jump_speed;
	}
	
	public void setVelocityX(float x) {
		this.linearVelocity.x = x;
	}
	
	public void setVelocityY(float y) {
		this.linearVelocity.y = y;
	}
	
	public void accelerateX(float x) {
		this.linearVelocity.x += x;
	}
	
	public void accelerateY(float y) {
		this.linearVelocity.y += y;
	}
}