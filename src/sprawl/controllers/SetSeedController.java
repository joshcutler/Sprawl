package sprawl.controllers;

import sprawl.Game;
import sprawl.states.PlayState;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class SetSeedController implements ScreenController{
	Nifty nifty;
	
	@Override
	public void bind(Nifty arg0, Screen arg1) {
		this.nifty = arg0;	
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub
	}
	
	public void newGame() {
		TextField textBox = nifty.getCurrentScreen().findNiftyControl("#txt_seed", TextField.class);
		int seed = 0;
		try {
			seed = Integer.parseInt(textBox.getRealText());
		} catch (Exception e) { }
		
		Game game = Game.currentGame;
		System.out.println(seed);
		game.changeState(new PlayState(game, seed));
	}
}
