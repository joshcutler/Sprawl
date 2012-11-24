package sprawl.states;

import sprawl.Game;
import de.lessvoid.nifty.Nifty;

public interface GameState {
	public void render(int delta, Game game);
	public void update(int delta, Game game);
	public void handleInput(Game game);
	public Nifty getNifty();
}
