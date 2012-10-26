package sprawl.world;

public class Biome {
	private BiomeType type;
	private int width;

	public Biome(BiomeType type, int width) {
		this.type = type;
		this.width = width;
	}
	
	public BiomeType getType() {
		return type;
	}

	public int getWidth() {
		return width;
	}
}
