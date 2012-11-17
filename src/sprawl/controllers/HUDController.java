package sprawl.controllers;

import sprawl.Game;
import sprawl.entities.PC;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class HUDController implements ScreenController {
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
}
