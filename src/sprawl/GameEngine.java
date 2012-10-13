package sprawl;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

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
	
	//32 pixels is one meter
	private static org.jbox2d.dynamics.World physics_world = new org.jbox2d.dynamics.World(new Vec2(0, -9.81f), true);
	private static Set<Body> bodies = new HashSet<Body>();
	
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
        //world = new World(new File("save.xml"));
        world = new World();
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
		
		setupObjects();
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
        	RenderingEngine.drawSelectionBox(camera);
        	updateFPS();
        	updatePhysics();
        	glPopMatrix();
        	
        	RenderingEngine.updateDisplay();
        }
 
        Display.destroy();
        System.exit(0);
	}
	
	private void updatePhysics() {
		physics_world.step(1/60f, 8, 3);
		
		for (Body body : bodies) {
			if (body.getType() == BodyType.DYNAMIC) {
				glPushMatrix();
				Vec2 bodyPosition = body.getPosition().mul(32);
				glTranslatef(bodyPosition.x, -bodyPosition.y, 0);
				glRectf(-0.75f * 32, -0.75f * 32, 0.75f * 32, 0.75f * 32);
				glPopMatrix();
			}
		}
	}
	
	private void setupObjects() {
		BodyDef boxDef = new BodyDef();
		boxDef.position.set(60, 60);
		boxDef.type = BodyType.DYNAMIC;
		
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(0.75f, 0.75f);
		Body box = physics_world.createBody(boxDef);
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 0.1f;
		boxFixture.shape = boxShape;
		box.createFixture(boxFixture);
		bodies.add(box);
	}
}
