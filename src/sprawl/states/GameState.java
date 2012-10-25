package sprawl.states;

import sprawl.Game;

public interface GameState {
	public void render(int delta, Game game);
	public void update(int delta, Game game);
	public void handleInput(Game game);
}
