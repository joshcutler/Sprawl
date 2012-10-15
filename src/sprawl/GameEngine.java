package sprawl;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.collision.shapes.MassData;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
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
		
		camera = new Camera(0, 0);
        physics = new PhysicsEngine();
		world = new World(physics, new File("save.xml"));
        pc = new PC();
        
        pc.setAt(16, 16);
        world.addEntity(pc);
        pc.getPhysicsBody().m_mass = 0f;
        
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
        	
        	InputEngine.handleInput(camera, pc); 
        	physics.update(delta);
        	world.draw();
        	RenderingEngine.drawEntities(camera, world);
        	RenderingEngine.drawSelectionBox(camera);
        	
        	if (drawPhysics) {
        		physics.drawPhysicsWorld();
        	}
        	
        	updateFPS();
        	glPopMatrix();
        	
        	RenderingEngine.updateDisplay();
        }
 
        Display.destroy();
        System.exit(0);
	}
}
