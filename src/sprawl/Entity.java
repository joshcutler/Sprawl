package sprawl;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.newdawn.slick.opengl.Texture;

public abstract class Entity implements PhysicsEntity {
	protected boolean hasPhysics = false;
	protected Body physics_body;
	protected int x;
	protected int y;
	protected int height;
	protected int width;
	protected float speed;
	protected float friction = 0;
	private int footContacts = 0;
	
	public boolean onSolidGround() {
		return (this.footContacts > 0);
	}
	
	public void incrementFootContacts() {
		this.footContacts++;
	}
	
	public void decrementFootContacts() {
		this.footContacts--;
	}
	
	public int getFootContacts() {
		return this.footContacts;
	}
	
	public float getFriction() {
		return friction;
	}
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public String texture_location;
    public Texture texture;
	
	public void setAt(int x, int y) {
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
	    	glVertex2f(0 + x, 0 + y);
	    	glTexCoord2f(1, 0);
	    	glVertex2f(width + x, 0 + y);
	    	glTexCoord2f(1, 1);
	    	glVertex2f(width + x, height + y);
	    	glTexCoord2f(0, 1);
	    	glVertex2f(0 + x, height + y);
	    glEnd();
	}
	
	public void bind() throws Exception {
		this.texture.bind();
	}
	
	public void setPhysicsBody(Body b) {
		this.physics_body = b;
	}
	
	public Body getPhysicsBody() {
		return this.physics_body;
	}
	
	public void move(Vec2 force, Vec2 position) {
		this.physics_body.applyForce(force, position);
	}
	
	public void registerSensors(PhysicsEngine physics) {
		
	}
}
