package sprawl.states;

import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.io.File;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import sprawl.Camera;
import sprawl.Constants;
import sprawl.Game;
import sprawl.GameTime;
import sprawl.HUD;
import sprawl.PhysicsEngine;
import sprawl.RenderingEngine;
import sprawl.entities.EntityDirection;
import sprawl.entities.KeyCommand;
import sprawl.entities.PC;
import sprawl.entities.PCLegsState;
import sprawl.items.Item;
import sprawl.world.World;
import sprawl.world.WorldGenerator;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class PlayState implements GameState {
	public Nifty nifty;
	private LwjglInputSystem inputSystem;
	
	public PlayState(Game game, int seed) {
		game.setCamera(new Camera());
		game.setPhysics(new PhysicsEngine());
		game.setWorld(WorldGenerator.generate(seed));
		GameTime.reset(0, 12, 0);
		game.setPC(new PC());
        
		game.getPC().moveTo(16, 16);
		game.getPhysics().registerObject(game.getPC());
		game.getWorld().addEntity(game.getPC());
		
		nifty = new Nifty(new LwjglRenderDevice(), new OpenALSoundDevice(), new LwjglInputSystem(), new AccurateTimeProvider());
		nifty.fromXml("guis/hud.xml", "hud");
		// nifty.setDebugOptionPanelColors(true);
		
		try {
	      inputSystem = new LwjglInputSystem();
	      inputSystem.startup();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
	public Nifty getNifty() {
		return nifty;
	}
	
	@Override
	public void render(int delta, Game game) {
		Camera camera = game.getCamera();
		World world = game.getWorld();
		PC pc = game.getPC();
		
		int tiles_drawn = world.draw(camera);
    	RenderingEngine.drawEntities(camera, world, delta);
    	RenderingEngine.drawLights(game);
    	RenderingEngine.drawSelectionBox(camera);
    	RenderingEngine.updateHUD(nifty, pc, tiles_drawn, world, game.getFPS());
    	
    	glPushMatrix();
    	glLoadIdentity();
    	nifty.render(false);
    	glPopMatrix();
	}

	@Override
	public void update(int delta, Game game) {
		Camera camera = game.getCamera();
		World world = game.getWorld();
		PhysicsEngine physics = game.getPhysics();
		PC pc = game.getPC();
		
    	GameTime.update(delta);
		physics.update(delta, world);
    	camera.update(pc, world);
    	
    	world.growPlants(delta, camera);
    	world.growGroundCover(delta, camera);
    	
    	nifty.update();
    	game.updateFPS();
	}

	@Override
	public void handleInput(Game game) {
		Camera camera = game.getCamera();
		PC pc = game.getPC();
		World world = game.getWorld();
		
		int mouse_x = Mouse.getX();
		int mouse_y = Constants.WINDOW_HEIGHT - Mouse.getY() - 1;
		boolean left_mouse_clicked = Mouse.isButtonDown(0);
		boolean right_mouse_clicked = Mouse.isButtonDown(1);
		
		Game.selector_x = Math.round(camera.translateX(mouse_x) / Constants.BLOCK_SIZE);
		Game.selector_y = Math.round(camera.translateY(mouse_y) / Constants.BLOCK_SIZE);

		if (right_mouse_clicked) {
			Item item = pc.getItemByHash(Game.selected_item);
			if (item != null) {
				if (item.getType().placeable && world.canPlace(Game.selector_x, Game.selector_y)) {
					item = pc.removeItemInstance(item.getHash());
					HUD.drawInventory();
					world.setAt(Game.selector_x, Game.selector_y, item.placeAs());
				}
			}
		}
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_P && Keyboard.getEventKeyState()) {
				Game.debug = !Game.debug;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_S) {
				// world.save(new File("save.xml"));
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_L) {
				try {
					world.load(new File("save.xml"));
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
				game.changeState(new PauseState());
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_R) {
				pc.setX(0);
				pc.setY(0);
				pc.moveTo(10, 10);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_M) {
				Game.drawPhysics = Game.drawPhysics ? false : true;
			}
			
			// Interact w/ HUD
			if (Keyboard.getEventKey() == Keyboard.KEY_I && Keyboard.getEventKeyState()) {
				HUD.toggleInventory();
			}
		}
		
		// Handle movement
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && pc.onSolidGround()) {
			if (KeyCommand.JUMP.isArmed()) {
				pc.setVelocityY(pc.getJumpSpeed());
				pc.setLegsState(PCLegsState.STANDING);
				KeyCommand.JUMP.resetArmed();
			}
		}
		KeyCommand.JUMP.updatePressed(Keyboard.isKeyDown(Keyboard.KEY_SPACE));
		
		boolean walking = false;
		if (Keyboard.isKeyDown(Keyboard.KEY_CAPITAL)) {
			walking = true;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			if (KeyCommand.MOVE_RIGHT.isArmed()) {
				pc.accelerateX(pc.getAcceleration(), walking);
				pc.changeDirection(EntityDirection.RIGHT);
				pc.setLegsState(PCLegsState.WALKING);
				KeyCommand.MOVE_RIGHT.resetArmed();
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if (KeyCommand.MOVE_LEFT.isArmed()) {
				pc.accelerateX(-pc.getAcceleration(), walking);
				pc.changeDirection(EntityDirection.LEFT);
				pc.setLegsState(PCLegsState.WALKING);
				KeyCommand.MOVE_LEFT.resetArmed();
			}
		} else {
			// Stop the dude from sliding
			if (pc.onSolidGround()) {
				pc.setVelocityX(0);
				pc.setLegsState(PCLegsState.STANDING);
			}
		}
		KeyCommand.MOVE_RIGHT.updatePressed(Keyboard.isKeyDown(Keyboard.KEY_D));
		KeyCommand.MOVE_LEFT.updatePressed(Keyboard.isKeyDown(Keyboard.KEY_A));
	}
}
