package sprawl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.TextureLoader;

public class RenderingEngine {
	public static int VBO_id;
	public static UnicodeFont font;
	
	public static void initOpenGL() {
		try {
			Display.setDisplayMode(new DisplayMode(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
            Display.setTitle(Constants.APPLICATION_NAME);
            Display.create();
            System.out.println("OpenGL Version: " + GL11.glGetString(GL11.GL_VERSION));
		} catch (LWJGLException e) {
            e.printStackTrace();
            Display.destroy();
        	System.err.println("Display wasn't initialized correctly.");
        	System.exit(1);
        }
			
		// Initialization code OpenGL
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	
        glViewport(0, 0, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
	}
	
	public static void initTextures() {
		for (BlockType block_type : BlockType.values()) {
			try {
				block_type.texture = TextureLoader.getTexture("PNG",
						RenderingEngine.class.getResourceAsStream(block_type.texture_location));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (LightSource light_source : LightSource.values()) {
			try {
				light_source.texture = TextureLoader.getTexture("PNG",
						RenderingEngine.class.getResourceAsStream(light_source.texture_location));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
    public static void initFonts() {
        java.awt.Font awtFont = new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 12);
        font = new UnicodeFont(awtFont);
        font.getEffects().add(new ColorEffect(java.awt.Color.white));
        font.addAsciiGlyphs();
        try {
            font.loadGlyphs();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
	
	public static void drawEntities(Camera camera, World world) {
		for (Entity e : world.getEntities()) {
			e.draw();
		}
	}
	
	public static void drawLights(Game game) {
		// Set lighting on each tile
		Camera camera = game.getCamera();
		World world = game.getWorld();
		
		//Load light mask		
	    glColorMask(false, false, false, true);
	    glBlendEquation(GL_MIN);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA);

	    LightSource.SHADOW.texture.bind();
		//Cull unneeded blocks and set light parameters
		int extra_lights = 10;
	    int left_edge = camera.leftVisibleBlockIndex() - extra_lights;
	    if (left_edge < 0) {
			left_edge = 0;
		}
		int right_edge = camera.rightVisibleBlockIndex() + extra_lights;
		if (right_edge >= Constants.WORLD_WIDTH) {
			right_edge = Constants.WORLD_WIDTH;
		}
		int top_edge = camera.topVisibleBlockIndex() - extra_lights;
		if (top_edge < 0) {
			top_edge = 0;
		}
		int bottom_edge = camera.bottomVisibleBlockIndex() + extra_lights;
		if (bottom_edge >= Constants.WORLD_HEIGHT) {
			bottom_edge = Constants.WORLD_HEIGHT;
		}
		float daylight = GameTime.daylight();
		for (int i = left_edge; i < right_edge - 1; i++) {
			for (int j = top_edge; j < bottom_edge - 1; j++) {
				Block b = world.getAt(i, j);
				float maxLight = 0;
				float attenuation = 1;
				
				if (b.getType() == BlockType.AIR) {
					maxLight = daylight;
				} else {
					//Get max light from a neighbor
					int left = i - 1;
					if (left < 0) {
						left = 0;
					}
					int right = i + 1;
					if (right >= Constants.WORLD_WIDTH - 1) {
						right = i - 1;
					}
					int top = j - 1;
					if (top < 0) {
						top = 0;
					}
					int bottom = j + 1;
					if (bottom >= Constants.WORLD_HEIGHT - 1) {
						bottom = j - 1;
					}
					
					for (int k = left; k <= right; k++) {
						for (int l = top; l <= bottom; l++) {
							Block neighbor = world.getAt(k, l);
							if (neighbor.getLight() * neighbor.getType().light_attenuation > maxLight * attenuation) {
								maxLight = neighbor.getLight();
								attenuation = neighbor.getType().light_attenuation;
							}
						}
					}
				}
				b.setLight(attenuation * maxLight);
					
				float x = b.getX();
				float y = b.getY();
				glColor4f(0, 0, 0, 1 - b.getLight());
				glBegin(GL_QUADS);
			    	glTexCoord2f(0, 0);
			    	glVertex2f(x , y);
			    	glTexCoord2f(1, 0);
			    	glVertex2f(x + Constants.BLOCK_SIZE, y);
			    	glTexCoord2f(1, 1);
			    	glVertex2f(x + Constants.BLOCK_SIZE, y + Constants.BLOCK_SIZE);
			    	glTexCoord2f(0, 1);
			    	glVertex2f(x, y + Constants.BLOCK_SIZE);
			    glEnd();
			}
		}
		
		// Draw the black shadow color
		glColor4f(1, 1, 1, 1);
		float lighting_buffer = Constants.BLOCK_SIZE * extra_lights;
	    glColorMask(true, true, true, false);
	    glBlendEquation(GL_FUNC_ADD);
	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA);
	    glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(-camera.getX() - lighting_buffer, -camera.getY() - lighting_buffer);
			glTexCoord2f(1, 0);
			glVertex2f(-camera.getX() + Constants.WINDOW_WIDTH + lighting_buffer, -camera.getY() - lighting_buffer);
			glTexCoord2f(1, 1);
			glVertex2f(-camera.getX() + Constants.WINDOW_WIDTH + lighting_buffer, -camera.getY()  + Constants.WINDOW_HEIGHT + lighting_buffer);
			glTexCoord2f(0, 1);
			glVertex2f(-camera.getX() - lighting_buffer, -camera.getY()  + Constants.WINDOW_HEIGHT + lighting_buffer);
	    glEnd();
	    glColorMask(true, true, true, true);
	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	// Old lighting algorithm
//	public static void drawLights1(Game game) {
//		Camera camera = game.getCamera();
//		World world = game.getWorld();
//		
//		//Load light mask		
//	    glColorMask(false, false, false, true);
//	    LightSource.SKY.texture.bind();
//	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA);
//	    glBlendEquation(GL_MIN);
//	    
//	    //Cull unneeded blocks and draw ambient sky light sources
//		int extra_lights = 8;
//	    int left_edge = camera.leftVisibleBlockIndex() - extra_lights;
//	    if (left_edge < 0) {
//			left_edge = 0;
//		}
//		int right_edge = camera.rightVisibleBlockIndex() + extra_lights;
//		if (right_edge >= Constants.WORLD_WIDTH) {
//			right_edge = Constants.WORLD_WIDTH;
//		}
//		int top_edge = camera.topVisibleBlockIndex() - extra_lights;
//		if (top_edge < 0) {
//			top_edge = 0;
//		}
//		int bottom_edge = camera.bottomVisibleBlockIndex() + extra_lights;
//		if (bottom_edge >= Constants.WORLD_HEIGHT) {
//			bottom_edge = Constants.WORLD_HEIGHT;
//		}
//		float radius = LightSource.SKY.distance * Constants.BLOCK_SIZE;
//		for (int i = left_edge; i < right_edge - 1; i++) {
//			for (int j = top_edge; j < bottom_edge - 1; j++) {
//				Block b = world.getAt(i, j);
//				if (b.getType() == BlockType.AIR) {
//					float x = b.getX() + b.getWidth() / 2;
//					float y = b.getY() + b.getHeight() / 2;
//					glBegin(GL_QUADS);
//				    	glTexCoord2f(0, 0);
//				    	glVertex2f(x - radius , y - radius);
//				    	glTexCoord2f(1, 0);
//				    	glVertex2f(x + radius, y - radius);
//				    	glTexCoord2f(1, 1);
//				    	glVertex2f(x + radius, y + radius);
//				    	glTexCoord2f(0, 1);
//				    	glVertex2f(x - radius, y + radius);
//				    glEnd();
//				}
//			}
//		}
//	    
//		//Draw overall light intensity
//		LightSource.SHADOW.texture.bind();
//		float lighting_buffer = Constants.BLOCK_SIZE * extra_lights;
//	    
//	    glColor4f(0, 0, 0, 1 - GameTime.daylight());
//	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_DST_ALPHA);
//	    glBlendEquation(GL_MAX);
//	    glBegin(GL_QUADS);
//			glTexCoord2f(0, 0);
//			glVertex2f(-camera.getX() - lighting_buffer, -camera.getY() - lighting_buffer);
//			glTexCoord2f(1, 0);
//			glVertex2f(-camera.getX() + Constants.WINDOW_WIDTH + lighting_buffer, -camera.getY() - lighting_buffer);
//			glTexCoord2f(1, 1);
//			glVertex2f(-camera.getX() + Constants.WINDOW_WIDTH + lighting_buffer, -camera.getY()  + Constants.WINDOW_HEIGHT + lighting_buffer);
//			glTexCoord2f(0, 1);
//			glVertex2f(-camera.getX() - lighting_buffer, -camera.getY()  + Constants.WINDOW_HEIGHT + lighting_buffer);
//	    glEnd();
//	    
//	    // Draw individual light sources
//	    glBlendEquation(GL_MIN);
//	    glColor4f(1, 1, 1, 1);
//	    for (Entity e : world.getEntities()) {
//	    	LightSource l = e.getLightSource();
//	    	if (l != null) {
//	    		l.texture.bind();
//	    		float x = e.getLightSourceX();
//	    		float y = e.getLightSourceY();
//	    		radius = e.getLightSourceRadius();
//			    glBegin(GL_QUADS);
//					glTexCoord2f(0, 0);
//					glVertex2f(x - radius , y - radius);
//					glTexCoord2f(1, 0);
//					glVertex2f(x + radius, y - radius);
//					glTexCoord2f(1, 1);
//					glVertex2f(x + radius, y + radius);
//					glTexCoord2f(0, 1);
//					glVertex2f(x - radius, y + radius);
//				glEnd();
//	    	}
//	    }
//	    
//	    
//		// Draw the black shadow color
//	    glColorMask(true, true, true, false);
//	    glBlendEquation(GL_FUNC_ADD);
//	    glBegin(GL_QUADS);
//			glTexCoord2f(0, 0);
//			glVertex2f(-camera.getX() - lighting_buffer, -camera.getY() - lighting_buffer);
//			glTexCoord2f(1, 0);
//			glVertex2f(-camera.getX() + Constants.WINDOW_WIDTH + lighting_buffer, -camera.getY() - lighting_buffer);
//			glTexCoord2f(1, 1);
//			glVertex2f(-camera.getX() + Constants.WINDOW_WIDTH + lighting_buffer, -camera.getY()  + Constants.WINDOW_HEIGHT + lighting_buffer);
//			glTexCoord2f(0, 1);
//			glVertex2f(-camera.getX() - lighting_buffer, -camera.getY()  + Constants.WINDOW_HEIGHT + lighting_buffer);
//	    glEnd();
//	    glColorMask(true, true, true, true);
//	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//	}
	
	public static void drawSelectionBox(Camera camera) {
		glColor4f(1f, 1f, 1f, 0.5f);
		new Block(Game.selection, Game.selector_x * Constants.BLOCK_SIZE, Game.selector_y * Constants.BLOCK_SIZE).draw();
		glColor4f(1f, 1f, 1f, 1f);
		
	}
	
	public static void updateDisplay() {
        Display.sync(60);
		Display.update();
	}
}
