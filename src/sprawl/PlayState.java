package sprawl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class PlayState implements GameState {
	public PlayState(Game game) {
		game.setCamera(new Camera());
		game.setPhysics(new PhysicsEngine());
		game.setWorld(new World(game.getPhysics(), new File("save.xml")));
		GameTime.reset(0, 12, 0);
		game.setPC(new PC());
        
		game.getPC().moveTo(16, 16);
		game.getWorld().addEntity(game.getPC());
	}

	@Override
	public void render(int delta, Game game) {
		Camera camera = game.getCamera();
		World world = game.getWorld();
		PC pc = game.getPC();
		
		int tiles_drawn = world.draw(camera);
    	RenderingEngine.drawEntities(camera, world);
    	RenderingEngine.drawLights(game);
    	RenderingEngine.drawSelectionBox(camera);
    	
    	renderDebug(tiles_drawn, camera, pc);
	}
	
	private void renderDebug(int tiles_drawn, Camera camera, PC pc) {
		RenderingEngine.font.drawString(camera.translateX(10), camera.translateY(20), "PC: " + pc.getX() + ", " + pc.getY());
		RenderingEngine.font.drawString(camera.translateX(10), camera.translateY(30), "Camera: " + camera.getX() + ", " + camera.getY());
		RenderingEngine.font.drawString(camera.translateX(10), camera.translateY(40), "Tiles: " + tiles_drawn);
		
		RenderingEngine.font.drawString(camera.translateX(Constants.WINDOW_WIDTH - 70), camera.translateY(10), GameTime.getDays() + "d " + GameTime.getHours() + "h " + GameTime.getMinutes() + " m" );
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
	}

	@Override
	public void handleInput(Game game) {
		Camera camera = game.getCamera();
		PC pc = game.getPC();
		World world = game.getWorld();
		
		int mouse_x = Mouse.getX();
		int mouse_y = Constants.WINDOW_HEIGHT - Mouse.getY() - 1;
		boolean mouse_clicked = Mouse.isButtonDown(0);
		
		Game.selector_x = Math.round(camera.translateX(mouse_x) / Constants.BLOCK_SIZE);
		Game.selector_y = Math.round(camera.translateY(mouse_y) / Constants.BLOCK_SIZE);

		
		if (mouse_clicked) {			
			world.setAt(Game.selector_x, Game.selector_y, Game.selection);
		}
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_S) {
				world.save(new File("save.xml"));
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_L) {
				try {
					world.load(new File("save.xml"));
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_1) {
				Game.selection = BlockType.STONE;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_2) {
				Game.selection = BlockType.DIRT;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_3) {
				Game.selection = BlockType.AIR;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_C) {
				world.clear();
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
				Display.destroy();
				System.exit(0);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_R) {
				pc.x = 0;
				pc.y = 0;
				pc.moveTo(10, 10);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_P) {
				if (KeyCommand.PAUSE.isArmed()) {
					game.changeState(new PauseState());
					KeyCommand.PAUSE.resetArmed();
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_M) {
				if (KeyCommand.DRAW_PHYSICS.isArmed()) {
					Game.drawPhysics = Game.drawPhysics ? false : true;
					KeyCommand.DRAW_PHYSICS.resetArmed();
				}
			}
		}
		KeyCommand.DRAW_PHYSICS.updatePressed(true);
		KeyCommand.PAUSE.updatePressed(true);
		
		// Handle movement
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && pc.onSolidGround()) {
			if (KeyCommand.JUMP.isArmed()) {
				pc.accelerateY(pc.jumpSpeed);
				KeyCommand.JUMP.resetArmed();
			}
		}
		KeyCommand.JUMP.updatePressed(Keyboard.isKeyDown(Keyboard.KEY_SPACE));
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			if (KeyCommand.MOVE_RIGHT.isArmed()) {
				pc.accelerateX(pc.acceleration);
				pc.changeDirection(EntityDirection.RIGHT);
				KeyCommand.MOVE_RIGHT.resetArmed();
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if (KeyCommand.MOVE_LEFT.isArmed()) {
				pc.accelerateX(-pc.acceleration);
				pc.changeDirection(EntityDirection.LEFT);
				KeyCommand.MOVE_LEFT.resetArmed();
			}
		} else {
			// Stop the dude from sliding
			if (pc.onSolidGround()) {
				pc.setVelocityX(0);
			}
		}
		KeyCommand.MOVE_RIGHT.updatePressed(Keyboard.isKeyDown(Keyboard.KEY_D));
		KeyCommand.MOVE_LEFT.updatePressed(Keyboard.isKeyDown(Keyboard.KEY_A));
	}
}
