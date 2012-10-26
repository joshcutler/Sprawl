package sprawl.world;

public enum BiomeType {
	FOREST(.8f, .1f), GRASSLAND(0f, 1f);
	
	public float treeGrowth;
	public float grassGrowth;
	public static int biomeWidth = 100;
	public static int biomeVariance = 10;
	
	BiomeType(float treeGrowth, float grassGrowth) {
		this.treeGrowth = treeGrowth;
		this.grassGrowth = grassGrowth;
	}
}
