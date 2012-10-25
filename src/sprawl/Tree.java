package sprawl;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Tree extends Vegetation {
	public static final int maxHeight = 10;
	public static final String texture_location = "/textures/tree";
	public static Texture texture;
	
	int height;
	 
	public Tree(int height) {
		this.height = height;
		
		if (Tree.texture == null) {
			try {
				Tree.texture = TextureLoader.getTexture("PNG", RenderingEngine.class.getResourceAsStream(Tree.texture_location + ".png"));
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
}
