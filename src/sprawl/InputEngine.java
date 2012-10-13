package sprawl;

import java.io.File;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class InputEngine {
	public static void handleInput(Camera camera) {
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
			if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT) {
				camera.moveRight(1);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_LEFT) {
				camera.moveLeft(1);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
				camera.moveUp(1);
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
				camera.moveDown(1);
			}
			
		}
	}
}
