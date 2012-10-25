package sprawl.entities;

import sprawl.Constants;
import sprawl.LightSource;
import sprawl.PhysicsType;

public class PC extends Entity {
	public PC() {
		this.height = Constants.BLOCK_SIZE * 4;
		this.width = Constants.BLOCK_SIZE * 2 - Constants.BLOCK_SIZE / 2;
		this.texture_location = "/textures/PC";
		this.acceleration = 2f;
		this.speed = 12f;
		this.jumpSpeed = 25f;
		this.direction = EntityDirection.LEFT;
		this.lightSource = LightSource.Entity;
		this.physicsType = PhysicsType.DYNAMIC;
		
		this.loadTexture();
	}
}
