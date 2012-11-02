package sprawl.controllers;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class SplashController  implements ScreenController, KeyInputHandler {
	private Nifty nifty;
	
	@Override
	public void bind(Nifty arg0, Screen arg1) {
		this.nifty = arg0;
	}

	@Override
	public void onEndScreen() {
	}

	@Override
	public void onStartScreen() {
		nifty.gotoScreen("menu");
	}

	@Override
	public boolean keyEvent(NiftyInputEvent inputEvent) {
		if (inputEvent == NiftyInputEvent.Escape) {
			nifty.setAlternateKey("exit");
		    nifty.gotoScreen("menu");
		    return true;
		}
		return false;
	}
}
