package sprawl;
import java.io.File;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
 
import static org.lwjgl.opengl.GL11.*;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}
	
	private BlockGrid grid;
	private BlockType selection = BlockType.STONE;
	private int selector_x = 0;
	private int selector_y = 0;
	private int camera_x = 0;
	private int camera_y = 0;
	
	public Main() {
		try {
            Display.setDisplayMode(new DisplayMode(640, 480));
            Display.setTitle(Constants.APPLICATION_NAME);
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            Display.destroy();
        	System.err.println("Display wasn't initialized correctly.");
        	System.exit(1);
        }
			
		// Initialization code OpenGL
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 640, 480, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        grid = new BlockGrid();
        grid.setAt(10,  10, BlockType.STONE);
        
        while (!Display.isCloseRequested()) {
        	glClear(GL_COLOR_BUFFER_BIT);
        	
        	glPushMatrix();
        	glTranslatef(camera_x, camera_y, 0);
        	
        	this.input(); 
        	grid.draw();
        	drawSelectionBox();
        	
        	glPopMatrix();
        	
        	
        	Display.update();
            Display.sync(60);
            
            System.out.println(camera_x + " " + camera_y);
        }
 
        Display.destroy();
        System.exit(0);
	}
	
	private void drawSelectionBox() {
		int x = selector_x * Constants.BLOCK_SIZE;
		int y = selector_y * Constants.BLOCK_SIZE;
		
		if (grid.getAt(selector_x,  selector_y).getType() != BlockType.AIR) {
			glBindTexture(GL_TEXTURE_2D, 0);
			glColor4f(1f, 1f, 1f, 0.5f);
			glBegin(GL_QUADS);
				glVertex2i(x, y);
				glVertex2i(x + Constants.BLOCK_SIZE, y);
				glVertex2i(x + Constants.BLOCK_SIZE, y + Constants.BLOCK_SIZE);
				glVertex2i(x, y + Constants.BLOCK_SIZE);
			glEnd();
			glColor4f(1f, 1f, 1f, 1f);
		} else if (selection == BlockType.AIR) {
			glBindTexture(GL_TEXTURE_2D, 0);
			glColor4f(1f, 1f, 1f, 0.5f);
			glBegin(GL_QUADS);
				glVertex2i(x, y);
				glVertex2i(x + Constants.BLOCK_SIZE, y);
				glVertex2i(x + Constants.BLOCK_SIZE, y + Constants.BLOCK_SIZE);
				glVertex2i(x, y + Constants.BLOCK_SIZE);
			glEnd();
			glColor4f(1f, 1f, 1f, 1f);
		} else {
			glColor4f(1f, 1f, 1f, 0.5f);
			new Block(selection, selector_x * Constants.BLOCK_SIZE, selector_y * Constants.BLOCK_SIZE).draw();
			glColor4f(1f, 1f, 1f, 1f);
		}
	}
	
	private void input() {
		int mouse_x = Mouse.getX();
		int mouse_y = 480 - Mouse.getY() - 1;
		boolean mouse_clicked = Mouse.isButtonDown(0);
		
		selector_x = Math.round(mouse_x / Constants.BLOCK_SIZE);
		selector_y = Math.round(mouse_y / Constants.BLOCK_SIZE);

		
		if (mouse_clicked) {			
			grid.setAt(selector_x, selector_y, selection);
		}
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_S) {
				grid.save(new File("save.xml"));
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_L) {
				grid.load(new File("save.xml"));
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_1) {
				selection = BlockType.STONE;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_2) {
				selection = BlockType.DIRT;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_3) {
				selection = BlockType.GRASS;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_4) {
				selection = BlockType.AIR;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_C) {
				grid.clear();
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
				Display.destroy();
				System.exit(0);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
				camera_x += 1;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
				camera_x -= 1;
			}
			
		}
	}

}
