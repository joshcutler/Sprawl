package sprawl;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Animation {
	protected Texture texture;
	protected String textureLocation;
	protected int frames = 0;
	protected int startFrame = 0;
	protected int stopFrame = 0;
	protected float duration = 0;
	protected int timer = 0;
	protected int currentFrame = 0;
	protected int height;
	protected int width;
	protected int rows;
	protected int columns;
	
	public Animation(String textureLocation, int height, int width, int startFrame, int stopFrame, float duration) {
		this.textureLocation = textureLocation;
		this.setFrames(startFrame, stopFrame, duration);
		this.height = height;
		this.width = width;
		
		this.timer = 0;
		this.currentFrame = 0;
	}
	
	public void init() {
		try {
			this.texture = TextureLoader.getTexture("PNG",
					RenderingEngine.class.getResourceAsStream(this.textureLocation));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Texture Loaded: " + this.textureLocation);
	}
	
	public boolean draw(float x, float y, boolean flipped, int delta) {
		//Update frame
		timer += delta;
		boolean cycled = (timer >= 1000 * duration);
		timer = (int) (timer % (1000 * duration));
		currentFrame = (int) Math.floor (timer / ((1000 * duration) / frames));
			
		try {
			this.texture.bind();
			int rows = this.texture.getTextureWidth() / this.width;
			int columns = this.texture.getTextureHeight() / this.height;
			
			float frameSize = 1f / columns;
			float vTextureOffset = (startFrame + currentFrame) * frameSize;
			
			if (flipped) { 
		    	glBegin(GL_QUADS);
		    		glTexCoord2f(1, vTextureOffset);
			    	glVertex2f(x, y);
			    	glTexCoord2f(0, vTextureOffset);
			    	glVertex2f(width + x, y);
			    	glTexCoord2f(0, vTextureOffset + frameSize);
			    	glVertex2f(width + x, height + y);
			    	glTexCoord2f(1, vTextureOffset + frameSize);
			    	glVertex2f(x, height + y);
			    glEnd();
			} else {
				glBegin(GL_QUADS);
		    		glTexCoord2f(0, vTextureOffset);
			    	glVertex2f(x, y);
			    	glTexCoord2f(1, vTextureOffset);
			    	glVertex2f(width + x, y);
			    	glTexCoord2f(1, vTextureOffset + frameSize);
			    	glVertex2f(width + x, height + y);
			    	glTexCoord2f(0, vTextureOffset + frameSize);
			    	glVertex2f(x, height + y);
			    glEnd();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cycled;
	}
	
	public void resetAnimation() {
		this.currentFrame = 0;
		this.timer = 0;
	}
	
	public void setFrames(int startFrame, int stopFrame, float duration) {
		this.resetAnimation();
		this.startFrame = startFrame;
		this.stopFrame = stopFrame;
		this.frames = stopFrame - startFrame + 1;
		this.duration = duration;
	}
}
