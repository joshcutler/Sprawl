package sprawl;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
 
import static org.lwjgl.opengl.GL11.*;

public class Game {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Game game = new Game();
		game.init();
		game.gameLoop();
	}
	
	private World world;
	private PC pc;
	private PhysicsEngine physics;
	private Camera camera;
	public static BlockType selection = BlockType.STONE;
	public static int selector_x = 0;
	public static int selector_y = 0;
	
	private long lastFPS;
	private long lastFrame;
	private int fps;
	private int last_fps = 0;
	private GameState currentState;
	private GameState previousState;
	
	public static boolean drawPhysics = false;
	
	
	
	public Game() {}
	
	private void init() {
		RenderingEngine.initOpenGL();
		getDelta();
        lastFPS = getTime();
        
        currentState = new PlayState(this);
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
		
	private void gameLoop() {
		lastFPS = getTime();
		
		RenderingEngine.initFonts();
		RenderingEngine.initTextures();
		
		while (!Display.isCloseRequested()) {
        	int delta = getDelta();
        	
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			glPushMatrix();
			
			currentState.handleInput(this);
			currentState.update(delta, this);
        	currentState.render(delta, this);
        	
        	updateFPS();
        	glPopMatrix();
        	
        	RenderingEngine.updateDisplay();
        }
 
        Display.destroy();
        System.exit(0);
	}

	public PC getPC() {
		return pc;
	}

	public void setPC(PC pc) {
		this.pc = pc;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public PhysicsEngine getPhysics() {
		return physics;
	}

	public void setPhysics(PhysicsEngine physics) {
		this.physics = physics;
	}
	
	public void changeState(GameState newState) {
		previousState = currentState;
		currentState = newState;
	}

	public GameState getPreviousState() {
		return previousState;
	}

	public GameState getCurrentState() {
		return currentState;
	}
}
