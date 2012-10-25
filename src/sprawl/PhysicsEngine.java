package sprawl;

import java.util.HashSet;
import java.util.Set;

public class PhysicsEngine {
	public static final int pixels_per_meter = 16;
	private static final float gravity = -40f;
	private static Set<Entity> dynamic_entities = new HashSet<Entity>();

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
			//Right Wall
			if (newY > world.getHeightInPixels() - e.getHeight() - 1) {
				newY = world.getHeightInPixels() - e.getHeight() - 1;
				newVelocity.y = 0;
			}
			
			// Check that we aren't hitting the ground
			if (newVelocity.y < 0) {
				for (int i = 0; i < e.widthBlocksSpanned(); i++) {
					//Check that there are no block in between current position and the target position
					for (int j = 0; j < numberOfBlocks(newY - e.getY()); j++) {
						Block b = world.blockAt((int) Math.floor(e.getX()) + Constants.BLOCK_SIZE * i + 1, e.getY() + e.getHeight() + Constants.BLOCK_SIZE * j + 1);
						if (Game.drawPhysics) {
							b.setTempType(BlockType.DIRT);
						}
						if (b != null && b.getType() != BlockType.AIR) {
							downCollisionBlock = b;
							break;
						}
					}
				}
			} else if (newVelocity.y > 0) {
				for (int i = 0; i < e.widthBlocksSpanned(); i++) {
					//Check that there are no block in between current position and the target position
					for (int j = 0; j < numberOfBlocks(e.getY() - newY); j++) {
						Block b = world.blockAt((int)Math.floor(e.getX()) + Constants.BLOCK_SIZE * i + 1, e.getY() - Constants.BLOCK_SIZE * j - 1);
						if (Game.drawPhysics) {
							b.setTempType(BlockType.DIRT);
						}
						if (b != null && b.getType() != BlockType.AIR) {
							upCollisionBlock = b;
							break;
						}
					}
				}
			}
			
			if (newVelocity.x < 0) {
				for (int i = 0; i < e.heightBlocksSpanned(); i++) {
					//Check that there are no block in between current position and the target position
					for (int j = 0; j < numberOfBlocks(e.getX() - newX); j++) {
						Block b = world.blockAt(newX - Constants.BLOCK_SIZE * j, e.getY() + 1 + Constants.BLOCK_SIZE * i);
						if (Game.drawPhysics) {
							b.setTempType(BlockType.DIRT);
						}
						if (b.getType() != BlockType.AIR) {
							leftCollisionBlock = b;
							break;
						}
					}
				}
			} else if (newVelocity.x > 0) {
				for (int i = 0; i < e.heightBlocksSpanned(); i++) {
					//Check that there are no block in between current position and the target position
					for (int j = 0; j < numberOfBlocks(newX - e.getX()); j++) {
						Block b = world.blockAt(newX + e.getWidth() + Constants.BLOCK_SIZE * j, e.getY() + 1 + Constants.BLOCK_SIZE * i);
						if (Game.drawPhysics) {
							b.setTempType(BlockType.DIRT);
						}
						if (b != null && b != null && b.getType() != BlockType.AIR) {
							rightCollisionBlock = b;
							break;
						}
					}
				}
			}
			
			// Stop you from bad vertical motion
			if (downCollisionBlock != null) {
				newVelocity.y = 0;
				newY = downCollisionBlock.getY() - e.getHeight() + 1;
			}
			if (upCollisionBlock != null) {
				newVelocity.y = 0;
				newY = upCollisionBlock.getY() + Constants.BLOCK_SIZE + 1;
			}
			
			// Stop you from bad horizontal motion
			if (leftCollisionBlock != null) {
				newVelocity.x = 0;
				newX = leftCollisionBlock.getX() + leftCollisionBlock.width + 1;
			}
			if (rightCollisionBlock != null) {
				newVelocity.x = 0;
				newX = rightCollisionBlock.getX() - (e.width + 1);
			}
			
			//Special Case the ground
			if (downCollisionBlock != null) {
				e.onSolidGround(true);
			} else {
				e.onSolidGround(false);
			}
			
			e.setLinearVelocity(newVelocity);
			e.setX(newX);
			e.setY(newY);
		}
	}
	
	public static int numberOfBlocks(float x) {
		return (int) Math.ceil(x / Constants.BLOCK_SIZE);
	}

	public void registerObject(Entity e) {
		if (e.physicsType == PhysicsType.DYNAMIC) {
			e.setLinearVelocity(new Vec2(0, 0));
			dynamic_entities.add(e);
		}
	}
}
