package sprawl.world;

public enum BiomeType {
	FOREST(		.1f, .1f, 2, .8f, .1f), 
	GRASSLAND(	.05f, .05f, 1,  0f,  1f);
	
	public float treeGrowth;
	public float grassGrowth;
	
	public static int biomeWidth = 100;
	public static int biomeVariance = 10;
	
	public float elevationUp;
	public float elevationDown;
	public int maxElevationChange;
	
	BiomeType(float up, float down, int maxChange, float treeGrowth, float grassGrowth) {
		this.elevationUp = up;
		this.elevationDown = down;
		this.maxElevationChange = maxChange;
		
		this.treeGrowth = treeGrowth;
		this.grassGrowth = grassGrowth;
	}
}
