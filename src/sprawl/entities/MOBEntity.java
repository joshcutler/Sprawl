package sprawl.entities;

import sprawl.LightSource;
import sprawl.PhysicsType;
import sprawl.world.World;

public class MOBEntity extends Killable {
	private MOBType type;
	
	public MOBEntity(MOBType type) {
		this.type = type;
		this.texture_location = type.texture_location;
		this.height = type.height;
		this.width = type.width;
		
		this.acceleration = type.acceleration;
		this.speed = type.speed;
		this.jumpSpeed = type.jumpSpeed;
		this.direction = EntityDirection.RIGHT;
		this.lightSource = LightSource.Entity;
		this.physicsType = PhysicsType.DYNAMIC_COLLISION;

		this.maxHealth = type.HP;
		this.health = type.HP;
	}

	@Override
	public void collidedWith(World world, Entity e) {
		// TODO Auto-generated method stub	
	}
	
	public boolean isMOB() {
		return true;
	}
}
