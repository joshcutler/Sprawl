package sprawl;

import java.io.File;

import org.jbox2d.common.Vec2;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class InputEngine {
	public static void handleInput(Camera camera, PC pc) {
		int mouse_x = Mouse.getX();
		int mouse_y = Constants.WINDOW_HEIGHT - Mouse.getY() - 1;
		boolean mouse_clicked = Mouse.isButtonDown(0);
		
		GameEngine.selector_x = Math.round(camera.translateX(mouse_x) / Constants.BLOCK_SIZE);
		GameEngine.selector_y = Math.round(camera.translateY(mouse_y) / Constants.BLOCK_SIZE);

		
		if (mouse_clicked) {			
			GameEngine.world.setAt(GameEngine.selector_x, GameEngine.selector_y, GameEngine.selection);
		}
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_S) {
				GameEngine.world.save(new File("save.xml"));
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_L) {
				GameEngine.world.load(new File("save.xml"));
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_1) {
				GameEngine.selection = BlockType.STONE;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_2) {
				GameEngine.selection = BlockType.DIRT;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_3) {
				GameEngine.selection = BlockType.GRASS;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_4) {
				GameEngine.selection = BlockType.AIR;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_C) {
				GameEngine.world.clear();
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
				Display.destroy();
				System.exit(0);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_P) {
				if (KeyCommand.DRAW_PHYSICS.isArmed()) {
					GameEngine.drawPhysics = GameEngine.drawPhysics ? false : true;
					KeyCommand.DRAW_PHYSICS.resetArmed();
				}
			}
		}
		KeyCommand.DRAW_PHYSICS.updatePressed(true);
		
		// Handle movement
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && pc.onSolidGround()) {
			if (KeyCommand.JUMP.isArmed()) {
				pc.move(new Vec2(0, -pc.getJumpSpeed()), pc.physics_body.getPosition());
				KeyCommand.JUMP.resetArmed();
			}
		}
		KeyCommand.JUMP.updatePressed(Keyboard.isKeyDown(Keyboard.KEY_SPACE));
		
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			if (KeyCommand.MOVE_RIGHT.isArmed()) {
				pc.move(new Vec2(pc.getSpeed(), 0), pc.physics_body.getPosition());
				KeyCommand.MOVE_RIGHT.resetArmed();
			}
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			if (KeyCommand.MOVE_LEFT.isArmed()) {
				pc.move(new Vec2(-pc.getSpeed(), 0), pc.physics_body.getPosition());
				KeyCommand.MOVE_LEFT.resetArmed();
			}
		} else {
			// Stop the dude from sliding
			if (pc.onSolidGround()) {
				pc.physics_body.setLinearVelocity(new Vec2(0, 0));
			}
		}
		KeyCommand.MOVE_RIGHT.updatePressed(Keyboard.isKeyDown(Keyboard.KEY_D));
		KeyCommand.MOVE_LEFT.updatePressed(Keyboard.isKeyDown(Keyboard.KEY_A));
	}
}