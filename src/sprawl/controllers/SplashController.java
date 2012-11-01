package sprawl.controllers;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class SplashController  implements ScreenController {
	private Nifty nifty;
	
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
		nifty.gotoScreen("menu");
	}
}
