package sprawl.vegetation;

import org.newdawn.slick.opengl.Texture;

import sprawl.PhysicsType;
import sprawl.blocks.Block;

public abstract class Vegetation {
	PhysicsType physicsType = PhysicsType.FOREGROUND;
	
	protected Block block;
	
	
	public Block getBlock() {
		return block;
	}


	public void setBlock(Block block) {
		this.block = block;
	}


	public abstract void draw();
}
