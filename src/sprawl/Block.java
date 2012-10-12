package sprawl;
import sprawl.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import static org.lwjgl.opengl.GL11.*;

public class Block {
	private BlockType type = BlockType.AIR;
    private Texture texture = null;
    private int x;
    private int y;
	
    public Block(BlockType type, int x, int y) {
		super();
		this.type = type;
		this.x = x;
		this.y = y;

		try {
			this.texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(type.texture_location)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public void draw() {
    	texture.bind();
    	glPushMatrix();
    	glTranslatef(x, y, 0);
    	glBegin(GL_QUADS);
    		glTexCoord2f(0, 0);
	    	glVertex2f(0, 0);
	    	glTexCoord2f(1, 0);
	    	glVertex2f(Constants.BLOCK_SIZE, 0);
	    	glTexCoord2f(1, 1);
	    	glVertex2f(Constants.BLOCK_SIZE, Constants.BLOCK_SIZE);
	    	glTexCoord2f(0, 1);
	    	glVertex2f(0, Constants.BLOCK_SIZE);
	    glEnd();
	    glPopMatrix();
    }
    
    public void bind() {
    	texture.bind();
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
