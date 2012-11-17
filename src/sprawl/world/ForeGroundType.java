package sprawl.world;

import org.newdawn.slick.opengl.Texture;

public enum ForeGroundType {
	TREE_TRUNK("/textures/tree.png", true);

	public final String texture_location;
	public Texture texture;
	public final boolean takesSpace;

	ForeGroundType(String texture_location, boolean takesSpace) {
		this.texture_location = texture_location;
		this.takesSpace = takesSpace;
	}
}
