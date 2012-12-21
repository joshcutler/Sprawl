package sprawl.world;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import sprawl.Constants;
import sprawl.RenderingEngine;
import sprawl.vegetation.CoverType;
import sprawl.vegetation.Vegetation;

public class Block {
	private BlockType type = BlockType.AIR;
	private BlockType tempType = null;
    private float light;
    private Vegetation vegetation;
    private CoverType coverType;
    private ForeGroundType foreGround;
    private int digHealth;
    private int chopHealth;
    private BlockTile blockTile = BlockTile.VERTICAL;
    
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
		if (vegetation != null) {
			vegetation.setBlock(this);
		}
	}

	public Block(BlockType type) {
		super();
		setType(type);
		this.light = 0;
	}
    
    public void draw(float x, float y) {
    	int numTiles = 32;
    	float tileSize = 1f / numTiles;
    	float hOffset = 0;
    	float vOffset = blockTile.ordinal() * tileSize;
    	if (this.type != BlockType.AIR) {
	    	this.bind();
	    	glBegin(GL_QUADS);
	    		glTexCoord2f(hOffset, vOffset);
		    	glVertex2f(0 + x, 0 + y);
		    	glTexCoord2f(hOffset + tileSize, vOffset);
		    	glVertex2f(Constants.BLOCK_SIZE + x, 0 + y);
		    	glTexCoord2f(hOffset + tileSize, vOffset + tileSize);
		    	glVertex2f(Constants.BLOCK_SIZE + x, Constants.BLOCK_SIZE + y);
		    	glTexCoord2f(hOffset, vOffset + tileSize);
		    	glVertex2f(0 + x, Constants.BLOCK_SIZE + y);
		    glEnd();
		    
		    if (this.coverType != null) {
		    	RenderingEngine.getTexture(this.coverType.texture_location).bind();
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
	    	RenderingEngine.getTexture(this.foreGround.texture_location).bind();
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
    		RenderingEngine.getTexture(this.tempType.texture_location).bind();
    		this.tempType = null;
    	} else {
    		RenderingEngine.getTexture(this.type.texture_location).bind();
    	}
    }
    
	public BlockType getType() {
		return type;
	}

	public void setType(BlockType type) {
		this.type = type;
		if (type != null) {
			this.digHealth = type.digHealth;
		}
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
		this.chopHealth = foreGround.chopHealth;
		this.foreGround = foreGround;
	}
	
	public int setDigDamage(int dmg) {
		this.digHealth -= dmg;
		return digHealth;
	}
	
	public int setChopDamage(int dmg) {
		this.chopHealth -= dmg;
		return chopHealth;
	}
	
	public int getChopHealth() {
		return this.chopHealth;
	}
	
	public void setBlockTile(BlockTile bTile) {
		this.blockTile = bTile;
	}
	
	public BlockTile getBlockTile() {
		return this.blockTile;
	}
}
