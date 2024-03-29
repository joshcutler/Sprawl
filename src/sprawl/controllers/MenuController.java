package sprawl.controllers;

import org.lwjgl.opengl.Display;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class MenuController implements ScreenController{
	private Nifty nifty;
	private Screen screen;
	
	@Override
	public void bind(Nifty niftyParam, Screen screenParam) {
		this.nifty = niftyParam;
	    this.screen = screenParam;
	}
	
	public void quit() {
		Display.destroy();
		System.exit(0);
	}
	
	public void newGame() {
		nifty.gotoScreen("setseed");
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
