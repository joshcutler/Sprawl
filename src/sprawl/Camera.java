package sprawl;

import static org.lwjgl.opengl.GL11.glTranslatef;

public class Camera {
	private int current_x;
	private int current_y;
	private int target_x;
	private int target_y;
	private boolean moving_x = false;
	private boolean moving_y = false;
	private float speed = 0.0001f;
	float t = 0;
	
	public Camera(int starting_x, int starting_y) {
		current_x = starting_x;
		current_y = starting_y;
		
	}
	
	public void update(int delta) {
		t += speed * delta;
		
		// Should we animate X?
		if (target_x != current_x) {
			int next_x = (int) lerp(current_x, target_x, t);
			//Are we done animating?
			if ((current_x < target_x && next_x >= target_x) || (current_x > target_x && next_x <= target_x)) {
				next_x = target_x;
				moving_x = false;
			}
			current_x = next_x;
		}
		
		// Should we animate Y?
		if (target_y != current_y) {
			int next_y = (int) lerp(current_y, target_y, t);
			//Are we done animating?
			if ((current_y < target_y && next_y >= target_y) || (current_y > target_y && next_y <= target_y)) {
				next_y = target_y;
				moving_y = false;
			}
			current_y = next_y;
		}
				
		current_y = (int) lerp(current_y, target_y, t);
		glTranslatef(current_x, current_y, 0);
	}
	
	public void moveLeft(int distance) {
		if (!moving_x) {
			int to_move = distance*Constants.BLOCK_SIZE;
			if (current_x + distance < 0) {
				target_x = current_x + to_move;
				moving_x = true;
			}
		}
	}
	
	public void moveRight(int distance) {
		if (!moving_x) {
			int to_move = distance*Constants.BLOCK_SIZE;
			if (current_x - to_move> -1*((Constants.WORLD_WIDTH - 1) * Constants.BLOCK_SIZE - Constants.WINDOW_WIDTH)) {
				target_x = current_x - to_move;
				moving_x = true;
			}
		}
	}
	
	public void moveUp(int distance) {
		if (!moving_y) {
			int to_move = distance*Constants.BLOCK_SIZE;
			if (current_y + distance < 0) {			
				target_y = current_y + to_move;
				moving_y = true;
			}
		}
	}
	
	public void moveDown(int distance) {
		if (!moving_y) {
			int to_move = distance*Constants.BLOCK_SIZE;
			if (current_y - to_move> -1*((Constants.WORLD_HEIGHT - 1) * Constants.BLOCK_SIZE - Constants.WINDOW_HEIGHT)) {
				target_y = current_y - to_move;
				moving_y = true;
			}
		}
	}
	
	public int currentX() {
		return current_x;
	}
	
	public int currentY() {
		return current_y;
	}
	
	public static float lerp(float a, float b, float t) {
      if (t < 0)
         return a;

      return a + t * (b - a);
   }
}
