package sprawl;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

import sprawl.entities.PC;
import sprawl.states.GameState;
import sprawl.states.MainMenuState;
import sprawl.states.PlayState;
import sprawl.world.World;

public class Game {
	public static final String version = "0.1"; 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (String s: args) {
			if (s.equals("debug")) {
				Game.debug = true;
			}
		}
		Game game = new Game();
		game.gameLoop();
	}
	
	private World world;
	private PC pc;
	private PhysicsEngine physics;
	private Camera camera;
	public static String selected_item;
	public static int selector_x = 0;
	public static int selector_y = 0;
	
	private long lastFPS;
	private long lastFrame;
	private int fps;
	private int last_fps = 0;
	
	public static GameState currentState;
	public static GameState previousState;
	public static Game currentGame;
	public static boolean debug = false;
	
	public static boolean drawPhysics = false;

	public Game() {
		/* Set lwjgl library path so that LWJGL finds the natives depending on the OS. */
        String osName = System.getProperty("os.name");
        // Get .jar dir. new File(".") and property "user.dir" will not work if .jar is called from
        // a different directory, e.g. java -jar /someOtherDirectory/myApp.jar
        String nativeDir = "";
        try {
            nativeDir = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch (URISyntaxException uriEx) {
            try {
                // Try to resort to current dir. May still fail later due to bad start dir.
                uriEx.printStackTrace();
                nativeDir = new File(".").getCanonicalPath();
            } catch (IOException ioEx) {
                // Completely failed
                ioEx.printStackTrace();
                System.exit(-1);
            }
        }
        // Append library subdir
        nativeDir += File.separator + "lib" + File.separator + "native" + File.separator;
        if (osName.startsWith("Windows")) {
            nativeDir += "windows";
        } else if (osName.startsWith("Linux") || osName.startsWith("FreeBSD")) {
            nativeDir += "linux";
        } else if (osName.startsWith("Mac OS X")) {
            nativeDir += "macosx";
        } else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
            nativeDir += "solaris";
        } else {
            System.exit(-1);
        }
        System.setProperty("org.lwjgl.librarypath", nativeDir);
        
        RenderingEngine.initOpenGL();
		getDelta();
        lastFPS = getTime();
        
        if (Game.debug) {
        	Game.currentState = new PlayState(this, 1);
        } else {
        	Game.currentState = new MainMenuState();
        }
        Game.currentGame = this;
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
		if (getTime() - lastFPS > 1000) {
			last_fps = fps;
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
		
	public void gameLoop() {
		lastFPS = getTime();
		
		RenderingEngine.initFonts();
		RenderingEngine.initTextures();
		
		while (!Display.isCloseRequested()) {
        	int delta = getDelta();
        	
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
			glPushMatrix();
			
			currentState.handleInput(delta, this);
			currentState.update(delta, this);
        	currentState.render(delta, this);
        	
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

	public int getFPS() {
		return last_fps;
	}
	
	public void reset() {
		HUD.reset();
	}
}
