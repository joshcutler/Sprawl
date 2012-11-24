package sprawl.controllers;

import org.lwjgl.opengl.Display;

import sprawl.Game;
import sprawl.states.MainMenuState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class PauseController implements ScreenController {
	private Nifty nifty;
	private Screen screen;
	
	@Override
	public void bind(Nifty niftyParam, Screen screenParam) {
		this.nifty = niftyParam;
	    this.screen = screenParam;
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub
		
	}
	
	public void mainMenu() {
		Game game = Game.currentGame;
		game.changeState(new MainMenuState());
	}
	
	public void resume() {
		nifty.gotoScreen("hud");
		Game game = Game.currentGame;
		game.changeState(game.getPreviousState());
	}
	
	public void quit() {
		Display.destroy();
		System.exit(0);
	}

}
