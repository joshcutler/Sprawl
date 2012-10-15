package sprawl;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;

import sprawl.Constants;

import static org.lwjgl.opengl.GL11.*;

public class Block implements PhysicsEntity{
	private BlockType type = BlockType.AIR;
	private Body physics_body;
    private int x;
    private int y;
    
    public Block(BlockType type, int x, int y) {
		super();
		this.type = type;
		this.x = x;
		this.y = y;
	}
    
    public void draw() {
    	this.bind();
    	glBegin(GL_QUADS);
    		glTexCoord2f(0, 0);
	    	glVertex2f(0 + x, 0 + y);
	    	glTexCoord2f(1, 0);
	    	glVertex2f(Constants.BLOCK_SIZE + x, 0 + y);
	    	glTexCoord2f(1, 1);
	    	glVertex2f(Constants.BLOCK_SIZE + x, Constants.BLOCK_SIZE + y);
	    	glTexCoord2f(0, 1);
	    	glVertex2f(0 + x, Constants.BLOCK_SIZE + y);
	    glEnd();
    }
    
    public void bind() {
    	this.type.texture.bind();
    }
    
	public BlockType getType() {
		return type;
	}

	public void setType(BlockType type) {
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public BodyType getPhysicsType() {
		return BodyType.STATIC;
	}

	@Override
	public float getDensity() {
		return 1;
	}

	@Override
	public int getWidth() {
		return Constants.BLOCK_SIZE;
	}

	@Override
	public int getHeight() {
		return Constants.BLOCK_SIZE;
	}

	@Override
	public void move(Vec2 force, Vec2 position) {
		// TODO Auto-generated method stub
	}
	
	public float getFriction() {
		return 10f;
	}

	@Override
	public Body getPhysicsBody() {
		return this.physics_body;
	}
	public void setPhysicsBody(Body body) {
		this.physics_body = body;
	}

	@Override
	public float getRestitution() {
		return 0.5f;
	}
	
	public float getMass() {
		return 1;
	}
}
