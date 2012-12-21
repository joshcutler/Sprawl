package sprawl.world;

import org.newdawn.slick.opengl.Texture;

import sprawl.items.ItemType;

public enum BlockType {
	STONE("/textures/block_stone.png", true, 0.3f, true, 30, ItemType.STONE_BLOCK), 
	AIR("/textures/block_air.png", false, 1f, false, 0, ItemType.AIR_BLOCK), 
	DIRT("/textures/block_dirt.png", true, 0.3f, true, 10, ItemType.DIRT_BLOCK),
	WOOD("/textures/block_wood.png", true, 0.3f, false, 0, ItemType.WOOD_BLOCK);

	public final String texture_location;
	public Texture texture;
	public final boolean has_physics;
	public float light_attenuation;
	public final boolean isDiggable;
	public final int digHealth;
	public final ItemType itemType; 

	BlockType(String texture_location, boolean has_physics, float light_attenuation, boolean diggable, int digHealth, ItemType itemType) {
		this.texture_location = texture_location;
		this.has_physics = has_physics;
		this.light_attenuation = light_attenuation;
		this.isDiggable = diggable;
		this.digHealth = digHealth;
		this.itemType = itemType;
	}
}
