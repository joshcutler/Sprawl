package sprawl.vegetation;

import org.newdawn.slick.opengl.Texture;

public enum CoverType {
	GRASS("/textures/grass.png");

	public final String texture_location;
	public Texture texture;

	CoverType(String texture_location) {
		this.texture_location = texture_location;
	}
}
