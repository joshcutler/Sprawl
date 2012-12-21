package sprawl.vegetation;

import org.newdawn.slick.opengl.Texture;

public enum CoverType {
	GRASS("/textures/grass.png", 0.1f);

	public final String texture_location;
	public final float growthRate;

	CoverType(String texture_location, float g) {
		this.texture_location = texture_location;
		this.growthRate = g;
	}
}
