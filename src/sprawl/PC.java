package sprawl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.TextureLoader;

public class PC extends Entity {
	public PC() {
		this.physicsType = PhysicsType.DYNAMIC;
		this.height = Constants.BLOCK_SIZE * 4;
		this.width = Constants.BLOCK_SIZE * 2 - Constants.BLOCK_SIZE / 2;
		this.texture_location = "res/textures/PC.png";
		this.speed = 12f;
		this.jumpSpeed = 25f;
		
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
}
