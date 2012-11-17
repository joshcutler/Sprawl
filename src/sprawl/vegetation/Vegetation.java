package sprawl.vegetation;

import sprawl.PhysicsType;
import sprawl.world.Block;
import sprawl.world.World;

public abstract class Vegetation {
	PhysicsType physicsType = PhysicsType.FOREGROUND;
	
	protected Block block;
	protected float growthRate = 0;
	protected int leftWidth = 0;
	protected int rightWidth = 0;
	protected int height = 0;
	
	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public abstract boolean grow();

	public float getGrowthRate() {
		return growthRate;
	}

	public void setGrowthRate(float g) {
		growthRate = g;
	}

	public int getLeftWidth() {
		return leftWidth;
	}

	public void setLeftWidth(int leftWidth) {
		this.leftWidth = leftWidth;
	}

	public int getRightWidth() {
		return rightWidth;
	}

	public void setRightWidth(int rightWidth) {
		this.rightWidth = rightWidth;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public abstract void updateForegroundBlocks(int x, int y, World world);
}
