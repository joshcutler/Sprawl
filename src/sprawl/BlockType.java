package sprawl;

import org.newdawn.slick.opengl.Texture;

public enum BlockType {
	STONE("res/textures/stone.png"), AIR("res/textures/air.png"), GRASS("res/textures/grass.png"), DIRT("res/textures/dirt.png");
	
	public final String texture_location;
    public Texture texture;
	
    BlockType(String texture_location) {
        this.texture_location = texture_location;
    }
}
