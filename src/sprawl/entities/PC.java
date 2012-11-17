package sprawl.entities;

import sprawl.Constants;
import sprawl.LightSource;
import sprawl.PhysicsType;

public class PC extends Killable{
	public PC() {
		this.height = Constants.BLOCK_SIZE * 4;
		this.width = Constants.BLOCK_SIZE * 2 - Constants.BLOCK_SIZE / 2;
		this.texture_location = "/textures/PC.png";
		this.acceleration = 2f;
		this.speed = 12f;
		this.walkSpeed = 4f;
		this.jumpSpeed = 25f;
		this.direction = EntityDirection.RIGHT;
		this.lightSource = LightSource.Entity;
		this.physicsType = PhysicsType.DYNAMIC;
		this.maxHealth = 10;
		this.health = 10;
		
		this.loadTexture();
	}
}
