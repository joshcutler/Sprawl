package sprawl;
import sprawl.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class Block {
	private BlockType type = BlockType.AIR;
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
}
