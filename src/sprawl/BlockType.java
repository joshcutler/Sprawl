package sprawl;

import org.newdawn.slick.opengl.Texture;

public enum BlockType {
	STONE("res/textures/stone.png", true), AIR("res/textures/air.png", false), DIRT("res/textures/dirt.png", true);

	public final String texture_location;
	public Texture texture;
	public final boolean has_physics;

	BlockType(String texture_location, boolean has_physics) {
		this.texture_location = texture_location;
		this.has_physics = has_physics;
	}
}
