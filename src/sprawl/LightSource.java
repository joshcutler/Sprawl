package sprawl;

import org.newdawn.slick.opengl.Texture;

public enum LightSource {
	SKY(4, "/textures/lightmask-inverse.png"), SHADOW(1, "/textures/shadow.png"), Entity(4, "/textures/lightmask.png");
	
	public final String texture_location;
	public Texture texture;
	public final int distance;
	
	LightSource(int distance, String texture_location) {
		this.distance = distance;
		this.texture_location = texture_location;
	}
}
