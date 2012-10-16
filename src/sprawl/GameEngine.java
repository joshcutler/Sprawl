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
	public static PC pc;
	public static PhysicsEngine physics;
	
	public static BlockType selection = BlockType.STONE;
	public static int selector_x = 0;
	public static int selector_y = 0;
	
	private long lastFPS;
	private long lastFrame;
	private int fps;
	private int last_fps = 0;
	
	public static boolean drawPhysics = false;
	
	public Camera camera;
	
	public GameEngine() {}
	
	private void start() {
		RenderingEngine.initOpenGL();
		
		camera = new Camera();
        physics = new PhysicsEngine();
		world = new World(physics, new File("save.xml"));
		GameTime.reset();
        pc = new PC();
        
        pc.moveTo(16, 16);
        world.addEntity(pc);
        
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
		RenderingEngine.font.drawString(camera.translateX(10), camera.translateY(10), "FPS: " + last_fps);
		if (getTime() - lastFPS > 1000) {
			last_fps = fps;
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	
	public void updateDebug(int tiles_drawn) {
		RenderingEngine.font.drawString(camera.translateX(10), camera.translateY(20), "PC: " + pc.getX() + ", " + pc.getY());
		RenderingEngine.font.drawString(camera.translateX(10), camera.translateY(30), "Camera: " + camera.getX() + ", " + camera.getY());
		RenderingEngine.font.drawString(camera.translateX(10), camera.translateY(40), "Tiles: " + tiles_drawn);
		
		RenderingEngine.font.drawString(camera.translateX(Constants.WINDOW_WIDTH - 70), camera.translateY(10), GameTime.getDays() + "d " + GameTime.getHours() + "h " + GameTime.getMinutes() + " m" );
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
        	
			InputEngine.handleInput(camera, pc); 
        	GameTime.update(delta);
			physics.update(delta, world);
        	camera.update(pc, world);
        	
        	int tiles_drawn = world.draw(camera);
        	RenderingEngine.drawEntities(camera, world);
        	RenderingEngine.drawSelectionBox(camera);
        	
        	updateFPS();
        	updateDebug(tiles_drawn);
        	glPopMatrix();
        	
        	RenderingEngine.updateDisplay();
        }
 
        Display.destroy();
        System.exit(0);
	}
}
