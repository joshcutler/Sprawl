package sprawl;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

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
			if (Keyboard.getEventKey() == Keyboard.KEY_P) {
				if (KeyCommand.PAUSE.isArmed()) {
					game.changeState(game.getPreviousState());
					KeyCommand.PAUSE.resetArmed();
				}
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_G) {
				if (KeyCommand.GENERATE_WORLD.isArmed()) {
					PC pc = game.getPC();
					pc.x = 0;
					pc.y = 0;
					pc.moveTo(10, 10);
					game.getWorld().generate(100);
					KeyCommand.GENERATE_WORLD.resetArmed();
				}
			}
		}
		KeyCommand.PAUSE.updatePressed(true);
		KeyCommand.GENERATE_WORLD.updatePressed(true);
	}
}
