package sprawl.states;

import org.lwjgl.input.Keyboard;

import sprawl.Camera;
import sprawl.Constants;
import sprawl.Game;
import sprawl.GameTime;
import sprawl.PhysicsEngine;
import sprawl.entities.KeyCommand;
import sprawl.entities.PC;
import sprawl.world.World;
import sprawl.world.WorldGenerator;
import de.lessvoid.nifty.Nifty;

public class PauseState implements GameState{
	Nifty nifty;
	
	@SuppressWarnings("unchecked")
	public PauseState() {
	}
	
	@Override
	public void render(int delta, Game game) {
		game.getPreviousState().render(delta, game);
	}

	@Override
	public void update(int delta, Game game) {
		if (this.nifty == null) {
			this.nifty = Game.previousState.getNifty();
			nifty.gotoScreen("pause");
		}
		
    	nifty.update();
    	game.updateFPS();
	}
	
	public Nifty getNifty() {
		return nifty;
	}
	
	@Override
	public void handleInput(int delta, Game game) {
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
				nifty.gotoScreen("hud");
				game.changeState(game.getPreviousState());
			}
		}
	}
}
