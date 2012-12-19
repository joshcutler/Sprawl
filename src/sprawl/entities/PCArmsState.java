package sprawl.entities;

public enum PCArmsState {
	SWINGING(0, 4, 0.5f, true), STANDING(0, 0, 1f, false);
	
	public final int startFrame;
	public final int stopFrame;
	public float duration;
	public final boolean single;
	
	PCArmsState(int startFrame, int stopFrame, float duration, boolean single) {
		this.startFrame = startFrame;
		this.stopFrame = stopFrame;
		this.single = single;
		this.duration = duration;
	}
}
