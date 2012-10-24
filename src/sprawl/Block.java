package sprawl;

import sprawl.Constants;

import static org.lwjgl.opengl.GL11.*;

public class Block extends Entity {
	private BlockType type = BlockType.AIR;
	private BlockType tempType = null;
    private float light;
	
    public Block(BlockType type, int x, int y) {
		super();
		this.type = type;
		this.x = x;
		this.y = y;
		this.width = Constants.BLOCK_SIZE;
		this.height = Constants.BLOCK_SIZE;
		this.speed = 0;
		this.physicsType = PhysicsType.STATIC;
		this.light = 0;
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
    	if (this.tempType != null) {
    		this.tempType.texture.bind();
    		this.tempType = null;
    	} else {
    		this.type.texture.bind();
    	}
    }
    
	public BlockType getType() {
		return type;
	}

	public void setType(BlockType type) {
		this.type = type;
	}
	
	public void setTempType(BlockType type) {
		this.tempType = type;
	}

	public float getLight() {
		return light;
	}

	public void setLight(float light) {
		this.light = light;
	}
}
