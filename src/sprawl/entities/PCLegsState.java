package sprawl.entities;

public enum PCLegsState {
	WALKING(0, 3, 1f), STANDING(0, 0, 1f);
	
	public final int startFrame;
	public final int stopFrame;
	public final float duration;
	
	PCLegsState(int startFrame, int stopFrame, float duration) {
		this.startFrame = startFrame;
		this.stopFrame = stopFrame;
		this.duration = duration;
	}
}
