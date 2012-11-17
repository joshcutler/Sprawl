package sprawl.states;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import sprawl.Camera;
import sprawl.Constants;
import sprawl.Game;
import sprawl.entities.KeyCommand;
import sprawl.entities.PC;
import sprawl.world.World;
import sprawl.world.WorldGenerator;

public class PauseState implements GameState{
	UnicodeFont pauseFont;
	
	@SuppressWarnings("unchecked")
	public PauseState() {
		java.awt.Font font = new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 32);
		pauseFont = new UnicodeFont(font);
		pauseFont.getEffects().add(new ColorEffect(java.awt.Color.RED));
		pauseFont.addAsciiGlyphs();
        try {
        	pauseFont.loadGlyphs();
        } catch (SlickException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public void render(int delta, Game game) {
		game.getPreviousState().render(delta, game);
		
		Camera camera = game.getCamera();
		pauseFont.drawString(camera.translateX(Constants.WINDOW_WIDTH / 2 - 30), camera.translateY(Constants.WINDOW_HEIGHT / 2 - 50), "Paused");
	}

	@Override
	public void update(int delta, Game game) {
		Camera camera = game.getCamera();
		World world = game.getWorld();
		PC pc = game.getPC();
		
    	camera.update(pc, world);
	}

	@Override
	public void handleInput(Game game) {
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
				game.changeState(game.getPreviousState());
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_G) {
				if (KeyCommand.GENERATE_WORLD.isArmed()) {
					PC pc = game.getPC();
					pc.setX(0);
					pc.setY(0);
					pc.moveTo(10, 10);
					game.setWorld(WorldGenerator.generate(1));
					KeyCommand.GENERATE_WORLD.resetArmed();
				}
			}
		}
		KeyCommand.GENERATE_WORLD.updatePressed(true);
	}
}
