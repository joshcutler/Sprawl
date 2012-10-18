package sprawl;

import org.newdawn.slick.opengl.Texture;

public enum LightSource {
	SKY(4, "res/textures/lightmask.png"), SHADOW(1, "res/textures/shadow.png"), Entity(4, "res/textures/lightmask.png");
	
	public final String texture_location;
	public Texture texture;
	public final int distance;
	
	LightSource(int distance, String texture_location) {
		this.distance = distance;
		this.texture_location = texture_location;
	}
}
