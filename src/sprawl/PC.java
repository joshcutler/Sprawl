package sprawl;

public class PC extends Entity {
	public PC() {
		this.physicsType = PhysicsType.DYNAMIC;
		this.height = Constants.BLOCK_SIZE * 4;
		this.width = Constants.BLOCK_SIZE * 2 - Constants.BLOCK_SIZE / 2;
		this.texture_location = "/textures/PC";
		this.acceleration = 1f;
		this.speed = 12f;
		this.jumpSpeed = 25f;
		this.direction = EntityDirection.LEFT;
		this.lightSource = LightSource.Entity;
		
		this.loadTexture();
	}
	
	public float getLightSourceRadius() {
		return 10f * Constants.BLOCK_SIZE;
	}
}
