package sprawl.items;

import org.newdawn.slick.opengl.Texture;

public enum ItemType {
	STONE_BLOCK("/textures/stone.png", true), 
	AIR_BLOCK("/textures/air.png", true), 
	DIRT_BLOCK("/textures/dirt.png", true);

	public final String texture_location;
	public Texture texture;
	public boolean stackable;

	ItemType(String texture_location, boolean stackable) {
		this.texture_location = texture_location;
	}
}
