package sprawl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jbox2d.dynamics.BodyType;
import org.newdawn.slick.opengl.TextureLoader;

public class PC extends Entity {
	private float jumpSpeed;
	
	public float getJumpSpeed() {
		return jumpSpeed;
	}

	public void setJumpSpeed(float jump_speed) {
		this.jumpSpeed = jump_speed;
	}

	public PC() {
		this.hasPhysics = true;
		this.height = Constants.BLOCK_SIZE * 3;
		this.width = Constants.BLOCK_SIZE;
		this.texture_location = "res/textures/PC.png";
		this.speed = 5f;
		this.jumpSpeed = 50f;
		try {
			this.texture = TextureLoader.getTexture("PNG",
					new FileInputStream(new File(
							this.texture_location)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void registerSensors(PhysicsEngine physics) {
		physics.registerFootSensor(this);
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public BodyType getPhysicsType() {
		return BodyType.DYNAMIC;
	}
	
	public float getDensity() {
		return 0.1f;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

}
