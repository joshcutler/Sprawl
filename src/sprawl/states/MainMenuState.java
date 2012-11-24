package sprawl.states;

import sprawl.Game;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.LwjglRenderDevice;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class MainMenuState implements GameState {
	
	private Nifty nifty;
	private LwjglInputSystem inputSystem;
	private static boolean showIntro = true;
	
	public MainMenuState() {
		nifty = new Nifty(new LwjglRenderDevice(), new OpenALSoundDevice(), new LwjglInputSystem(), new AccurateTimeProvider());
		nifty.fromXml("guis/mainmenu.xml", (!MainMenuState.showIntro || Game.debug ? "menu" : "start"));
		MainMenuState.showIntro = false;
		//nifty.setDebugOptionPanelColors(true);
		
		if (Game.currentGame != null) {
			Game.currentGame.reset();
		}
		
		try {
	      inputSystem = new LwjglInputSystem();
	      inputSystem.startup();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	}
	
	@Override
	public void render(int delta, Game game) {
		// TODO Auto-generated method stub
		nifty.render(true);
	}

	@Override
	public void update(int delta, Game game) {
		// TODO Auto-generated method stub
		nifty.update();
	}

	@Override
	public void handleInput(Game game) {
		
	}
	
	public Nifty getNifty() {
		return nifty;
	}

}
