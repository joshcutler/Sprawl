package sprawl.vegetation;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import sprawl.Constants;
import sprawl.RenderingEngine;

public class Tree extends Vegetation {
	public static final int maxHeight = 20;
	public static final String texture_location = "/textures/tree";
	public static Texture texture;
	
	int height;
	 
	public Tree(int height) {
		this.height = height;
		this.growthRate = 0.01f;
		
		if (Tree.texture == null) {
			try {
				Tree.texture = TextureLoader.getTexture("PNG", RenderingEngine.class.getResourceAsStream(Tree.texture_location + ".png"));
				System.out.println("Texture Loaded: Tree.png");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void draw() {
		Tree.texture.bind();
		
		float startX = block.getX();
		float startY = block.getY();
		float width = Constants.BLOCK_SIZE;
		float height = Constants.BLOCK_SIZE;
		for (int i = 1; i <= this.height; i++) {
			float x = startX;
			float y = startY - Constants.BLOCK_SIZE * i;
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
	}
	
	public void grow() {
		if (this.height + 1 < Tree.maxHeight && Math.random() < growthRate) {
			this.height++;
		}
	}
}
