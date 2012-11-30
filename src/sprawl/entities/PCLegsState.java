package sprawl.entities;

public enum PCLegsState {
	WALKING(0, 3), STANDING(0, 0);
	
	public final int startFrame;
	public final int stopFrame;
	
	PCLegsState(int startFrame, int stopFrame) {
		this.startFrame = startFrame;
		this.stopFrame = stopFrame;
	}
}
