package sprawl;

import java.io.File;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
 
import static org.lwjgl.opengl.GL11.*;

public class GameEngine {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GameEngine game = new GameEngine();
		game.start();
		game.gameLoop();
	}
	
	public static World world;
	public static BlockType selection = BlockType.STONE;
	public static int selector_x = 0;
	public static int selector_y = 0;
	
	private long lastFPS;
	private long lastFrame;
	private int fps;
	private int last_fps = 0;
	
	public Camera camera;
	
	public GameEngine() {}
	
	private void start() {
		RenderingEngine.initOpenGL();
		
		camera = new Camera(0, 0);
        world = new World(new File("save.xml"));
        getDelta();
        lastFPS = getTime();
	}
	
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		return delta;
	}
	
	public void updateFPS() {
		RenderingEngine.font.drawString(10 - camera.currentX(), 10 - camera.currentY(), "FPS: " + last_fps);
		if (getTime() - lastFPS > 1000) {
			last_fps = fps;
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	private void gameLoop() {
		lastFPS = getTime();
		
		RenderingEngine.initFonts();
		RenderingEngine.initGeometry();
		RenderingEngine.initTextures();
		
		while (!Display.isCloseRequested()) {
        	int delta = getDelta();
        	
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			glPushMatrix();
        	camera.update(delta);
        	
        	InputEngine.handleInput(camera); 
        	world.draw();
        	RenderingEngine.drawSelectionBox();
        	updateFPS();
        	glPopMatrix();
        	
        	RenderingEngine.updateDisplay();
        }
 
        Display.destroy();
        System.exit(0);
	}
}
