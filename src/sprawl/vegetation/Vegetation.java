package sprawl.vegetation;

import sprawl.PhysicsType;
import sprawl.world.Block;

public abstract class Vegetation {
	PhysicsType physicsType = PhysicsType.FOREGROUND;
	
	protected Block block;
	protected float growthRate = 0;
	
	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public abstract void draw(int x, int y);
	public abstract void grow();

	public float getGrowthRate() {
		return growthRate;
	}

	public void setGrowthRate(float g) {
		growthRate = g;
	}
}
