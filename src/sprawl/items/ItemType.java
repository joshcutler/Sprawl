package sprawl.items;

import org.newdawn.slick.opengl.Texture;

public enum ItemType {
	STONE_BLOCK("/textures/stone.png", true, true), 
	AIR_BLOCK("/textures/air.png", true, true), 
	DIRT_BLOCK("/textures/dirt.png", true, true),
	WOOD_BLOCK("/textures/item_wood_block.png", true, true);

	public final String texture_location;
	public Texture texture;
	public boolean stackable;
	public boolean placeable;

	ItemType(String texture_location, boolean stackable, boolean placeable) {
		this.texture_location = texture_location;
		
		this.stackable = stackable;
		this.placeable = placeable;
	}
}
