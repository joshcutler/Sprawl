package sprawl.vegetation;

import sprawl.world.ForeGroundType;
import sprawl.world.World;

public class Tree extends Vegetation {
	public static final int maxHeight = 20;
	 
	public Tree(int height) {
		this.height = height;
		this.growthRate = 0.001f;
	}
	
	public boolean grow() {
		if (this.height + 1 < Tree.maxHeight) {
			this.height++;
			return true;
		}
		return false;
	}

	@Override
	public void updateForegroundBlocks(int x, int y, World world) {
		//Draw up the height of the tree
		for (int i = 1; i <= this.height; i++) {
			world.getAt(x, y - i).setForeGround(ForeGroundType.TREE_TRUNK);
		}
	}
	
	public String debugInfo() {
		return getName() + "-" + this.height;
	}
}
