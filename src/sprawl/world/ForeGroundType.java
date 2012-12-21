package sprawl.world;

import org.newdawn.slick.opengl.Texture;

import sprawl.items.ItemType;

public enum ForeGroundType {
	TREE_TRUNK("/textures/tree.png", true, true, 10, ItemType.WOOD_BLOCK);

	public final String texture_location;
	public final boolean takesSpace;
	public boolean isChoppable;
	public final int chopHealth;
	public final ItemType itemType;

	ForeGroundType(String texture_location, boolean takesSpace, boolean choppable, int chopHealth, ItemType itemType) {
		this.texture_location = texture_location;
		this.takesSpace = takesSpace;
		this.isChoppable = choppable;
		this.chopHealth = chopHealth;
		this.itemType = itemType;
	}
}
