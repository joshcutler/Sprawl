package sprawl.world;

import org.newdawn.slick.opengl.Texture;

public enum BlockType {
	STONE("/textures/stone.png", true, 0.3f), 
	AIR("/textures/air.png", false, 1f), 
	DIRT("/textures/dirt.png", true, 0.3f);

	public final String texture_location;
	public Texture texture;
	public final boolean has_physics;
	public float light_attenuation;

	BlockType(String texture_location, boolean has_physics, float light_attenuation) {
		this.texture_location = texture_location;
		this.has_physics = has_physics;
		this.light_attenuation = light_attenuation;
	}
}
