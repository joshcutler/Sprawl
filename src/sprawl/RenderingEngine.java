package sprawl;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileInputStream;
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
						new FileInputStream(new File(
								block_type.texture_location)));
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
	
	public static void initGeometry() {
		//TODO
	}
	
	public static void drawSelectionBox(Camera camera) {
		glColor4f(1f, 1f, 1f, 0.5f);
		new Block(GameEngine.selection, GameEngine.selector_x * Constants.BLOCK_SIZE, GameEngine.selector_y * Constants.BLOCK_SIZE).draw();
		glColor4f(1f, 1f, 1f, 1f);
		
	}
	
	public static void updateDisplay() {
        //Display.sync(60);
		Display.update();
	}
}
