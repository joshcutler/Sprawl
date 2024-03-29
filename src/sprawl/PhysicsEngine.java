package sprawl;

import java.util.HashSet;
import java.util.Set;

import sprawl.entities.Entity;
import sprawl.world.Block;
import sprawl.world.BlockType;
import sprawl.world.World;

public class PhysicsEngine {
	public static final int pixels_per_meter = 16;
	private static final float gravity = -60f;
	private static Set<Entity> dynamic_entities = new HashSet<Entity>();
	private static Set<Entity> collision_entities = new HashSet<Entity>();
	private static Set<Entity> remove_entities = new HashSet<Entity>();

	public PhysicsEngine() {
	}

	public void update(int delta, World world) {
		float step_size = 1 / 1000f * (float) delta; // Fraction of a second

		// Loop through all the entities
		for (Entity e : dynamic_entities) {

			// Apply gravity
			Vec2 currentVelocity = e.getLinearVelocity();
			Vec2 newVelocity = new Vec2(currentVelocity.x, currentVelocity.y + (gravity * step_size));
			float newX = e.getX() + newVelocity.x * step_size * pixels_per_meter;
			float newY = e.getY() - newVelocity.y * step_size* pixels_per_meter;
			
			// Block Collision Detection
			Block downCollisionBlock = null;
			Block upCollisionBlock = null;
			Block rightCollisionBlock = null;
			Block leftCollisionBlock = null;
			int downY = 0, upY = 0, leftX = 0, rightX = 0;
			
			//Left Wall
			if (newX < 1) {
				newX = 1;
				newVelocity.x = 0;
			}
			//Right Wall
			if (newX > world.getWidthInPixels() - e.getWidth() - 1) {
				newX = world.getWidthInPixels() - e.getWidth() - 1;
				newVelocity.x = 0;
			}
			//Top Wall
			if (newY < 1) {
				newY = 1;
				newVelocity.y = 0;
			}
			//Bottom Wall
			if (newY > world.getHeightInPixels() - e.getHeight() - 1) {
				newY = world.getHeightInPixels() - e.getHeight() - 1;
				newVelocity.y = 0;
			}
			
			// Check that we aren't hitting the ground
			if (newVelocity.y < 0) {
				for (int i = 0; i < e.widthBlocksSpanned(); i++) {
					//Check that there are no block in between current position and the target position
					for (int j = 0; j < numberOfBlocks(newY - e.getY()); j++) {
						int x = (int) Math.floor(e.getX()) + Constants.BLOCK_SIZE * i + 1;
						int y = (int) e.getY() + e.getHeight() + Constants.BLOCK_SIZE * j + 1;
						Block b = world.blockAt(x, y);
						if (Game.drawPhysics) {
							b.setTempType(BlockType.DIRT);
						}
						if (b != null && b.getType() != BlockType.AIR) {
							downCollisionBlock = b;
							downY = (int) world.blockStartsAt(x, y).y;
							break;
						}
					}
				}
			} else if (newVelocity.y > 0) {
				for (int i = 0; i < e.widthBlocksSpanned(); i++) {
					//Check that there are no block in between current position and the target position
					for (int j = 0; j < numberOfBlocks(e.getY() - newY); j++) {
						int x = (int) Math.floor(e.getX()) + Constants.BLOCK_SIZE * i + 1;
						int y = (int) e.getY() - Constants.BLOCK_SIZE * j - 1;
						Block b = world.blockAt(x, y);
						if (Game.drawPhysics) {
							b.setTempType(BlockType.DIRT);
						}
						if (b != null && b.getType() != BlockType.AIR) {
							upCollisionBlock = b;
							upY = (int) world.blockStartsAt(x, y).y;
							break;
						}
					}
				}
			}
			
			if (newVelocity.x < 0) {
				for (int i = 0; i < e.heightBlocksSpanned(); i++) {
					//Check that there are no block in between current position and the target position
					for (int j = 0; j < numberOfBlocks(e.getX() - newX); j++) {
						int x = (int) newX - Constants.BLOCK_SIZE * j;
						int y = (int) e.getY() + 1 + Constants.BLOCK_SIZE * i;
						Block b = world.blockAt(x, y);
						if (Game.drawPhysics) {
							b.setTempType(BlockType.DIRT);
						}
						if (b.getType() != BlockType.AIR) {
							leftCollisionBlock = b;
							leftX = (int) world.blockStartsAt(x, y).x;
							break;
						}
					}
				}
			} else if (newVelocity.x > 0) {
				for (int i = 0; i < e.heightBlocksSpanned(); i++) {
					//Check that there are no block in between current position and the target position
					for (int j = 0; j < numberOfBlocks(newX - e.getX()); j++) {
						int x = (int) newX + e.getWidth() + Constants.BLOCK_SIZE * j;
						int y = (int) e.getY() + 1 + Constants.BLOCK_SIZE * i;
						Block b = world.blockAt(x, y);
						if (Game.drawPhysics) {
							b.setTempType(BlockType.DIRT);
						}
						if (b != null && b != null && b.getType() != BlockType.AIR) {
							rightCollisionBlock = b;
							rightX = (int) world.blockStartsAt(x, y).x;
							break;
						}
					}
				}
			}
			
			// Stop you from bad vertical motion
			if (downCollisionBlock != null) {
				newVelocity.y = 0;
				newY = downY - e.getHeight() + 1;
			}
			if (upCollisionBlock != null) {
				newVelocity.y = 0;
				newY = upY + Constants.BLOCK_SIZE + 1;
			}
			
			// Stop you from bad horizontal motion
			if (leftCollisionBlock != null) {
				newVelocity.x = 0;
				newX = leftX + Constants.BLOCK_SIZE + 1;
				e.setFacingWall(true);
			} else if (rightCollisionBlock != null) {
				e.setFacingWall(true);
				newVelocity.x = 0;
				newX = rightX - (e.getWidth() + 1);
			} else {
				e.setFacingWall(false);
			}
				
			//Special Case the ground
			if (downCollisionBlock != null) {
				e.onSolidGround(true);
				if (e.preventSlide) {
					newVelocity.x = 0;
				}
			} else {
				e.onSolidGround(false);
			}
			
			e.setLinearVelocity(newVelocity);
			e.setX(newX);
			e.setY(newY);
		}
		
		for (Entity collider : collision_entities) {
			for (Entity target : dynamic_entities) {
				if (collider != target && collidesWithEntity(collider, target)) {
					target.collidedWith(world, collider);
				}
			}
		}
		
		for (Entity e : remove_entities) {
			dynamic_entities.remove(e);
			collision_entities.remove(e);
		}
		remove_entities.clear();
	}
	
	public static int numberOfBlocks(float x) {
		return (int) Math.ceil(x / Constants.BLOCK_SIZE);
	}

	public void registerObject(Entity e) {
		if (e.getPhysicsType() == PhysicsType.DYNAMIC) {
			e.setLinearVelocity(new Vec2(0, 0));
			dynamic_entities.add(e);
		}
		if (e.getPhysicsType() == PhysicsType.DYNAMIC_COLLISION) {
			e.setLinearVelocity(new Vec2(0, 0));
			dynamic_entities.add(e);
			collision_entities.add(e);
		}
	}
	
	public void markForRemoval(Entity e) {
		remove_entities.add(e);
	}
	
	public static boolean collidesWithBlock(int x, int y, Entity e) {
		Vec2 blockCoords = World.getBlockCoordinates(x, y);
		boolean xCollision = false, yCollision = false;
		if ((e.getX() >= blockCoords.x && e.getX() <= (blockCoords.x + Constants.BLOCK_SIZE)) || // Left side is in area
			((e.getX() + e.getWidth()) >= blockCoords.x && (e.getX() + e.getWidth()) <= (blockCoords.x + Constants.BLOCK_SIZE)) || // Right side is in area
			(e.getX() <= blockCoords.x && (e.getX() + e.getWidth()) >= (blockCoords.x + Constants.BLOCK_SIZE))) { // Spans area
			 	xCollision = true;
		}
		if ((e.getY() >= blockCoords.y && e.getY() <= (blockCoords.y + Constants.BLOCK_SIZE)) || // top side is in area
			((e.getY() + e.getHeight()) >= blockCoords.y && (e.getY() + e.getHeight()) <= (blockCoords.y + Constants.BLOCK_SIZE)) || // Bottom side is in area
			(e.getY() <= blockCoords.y && (e.getY() + e.getHeight()) >= (blockCoords.y + Constants.BLOCK_SIZE))) { // Spans area
				yCollision = true;
		}
		
		return (xCollision && yCollision);
	}
	
	public static boolean collidesWithEntity(Entity collider, Entity target) {
		boolean xCollision = false, yCollision = false;
		
		if ((collider.getX() >= target.getX() && collider.getX() <= (target.getX() + target.getWidth())) || // Left side is in area
			((collider.getX() + collider.getWidth()) >= target.getX() && (collider.getX() + collider.getWidth()) <= (target.getX() + target.getWidth())) || // Right side is in area
			(collider.getX() <= target.getX() && (collider.getX() + collider.getWidth()) >= (target.getX() + target.getWidth()))) { // Spans area
			 	xCollision = true;
		}
		if ((collider.getY() >= target.getY() && collider.getY() <= (target.getY() + target.getHeight())) || // top side is in area
			((collider.getY() + collider.getHeight()) >= target.getY() && (collider.getY() + collider.getHeight()) <= (target.getY() + target.getHeight())) || // Bottom side is in area
			(collider.getY() <= target.getY() && (collider.getY() + collider.getHeight()) >= (target.getY() + target.getHeight()))) { // Spans area
				yCollision = true;
		}
		return (xCollision && yCollision);
	}
}
