package sprawl.world;

import sprawl.Constants;
import sprawl.vegetation.CoverType;
import sprawl.vegetation.Vegetation;

import static org.lwjgl.opengl.GL11.*;

public class Block {
	private BlockType type = BlockType.AIR;
	private BlockType tempType = null;
    private float light;
    private Vegetation vegetation;
    private CoverType coverType;
    private ForeGroundType foreGround;
	
    public CoverType getCoverType() {
		return coverType;
	}

	public void setCoverType(CoverType groundCover) {
		this.coverType = groundCover;
	}

	public Vegetation getVegetation() {
		return vegetation;
	}

	public void setVegetation(Vegetation vegetation) {
		this.vegetation = vegetation;
		vegetation.setBlock(this);
	}

	public Block(BlockType type) {
		super();
		this.type = type;
		this.light = 0;
	}
    
    public void draw(float x, float y) {
    	if (this.type != BlockType.AIR) {
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
		    
		    if (this.coverType != null) {
		    	this.coverType.texture.bind();
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
    	}
	    
	    if (this.foreGround != null) {
	    	this.foreGround.texture.bind();
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

	public ForeGroundType getForeGround() {
		return foreGround;
	}

	public void setForeGround(ForeGroundType foreGround) {
		this.foreGround = foreGround;
	}
}
