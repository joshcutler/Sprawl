package sprawl.entities;

import sprawl.Constants;

public enum MOBType {
	DORK("/textures/mob_PC.png", 1, 3, 2, 2f, 12f, 25f);
	
	public final String texture_location;
	public final int HP;
	public final int height;
	public final int width;
	public final float speed;
	public final float acceleration;
	public final float jumpSpeed;
	
	MOBType(String texture, int hp, int h, int w, float s, float a, float js) {
		this.texture_location = texture;
		this.HP = hp;
		this.height = h * Constants.BLOCK_SIZE;
		this.width = w * Constants.BLOCK_SIZE;
		this.speed = s;
		this.acceleration = a;
		this.jumpSpeed = js;
	}
}
