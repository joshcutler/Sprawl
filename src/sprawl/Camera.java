package sprawl;

import static org.lwjgl.opengl.GL11.glTranslatef;

public class Camera {
	public int getX() {
		return current_x;
	}

	public int getY() {
		return current_y;
	}

	private int current_x;
	private int current_y;
	float t = 0;
	
	public Camera() {
		current_x = 0;
		current_y = 0;
		
	}
	
	public void update(PC pc, World world) {
		int window_center_x = Constants.WINDOW_WIDTH / 2;
		int window_center_y = Constants.WINDOW_HEIGHT / 2;
				
		int target_x = -(int) pc.getX() + pc.getWidth() / 2;
		int target_y = -(int) pc.getY() + pc.getHeight() / 2;
		
		if (target_x > -window_center_x) {
			target_x = 0;
		} else if (target_x < -((Constants.WORLD_WIDTH - 1) * Constants.BLOCK_SIZE - Constants.WINDOW_WIDTH / 2)) {
			target_x = -((Constants.WORLD_WIDTH - 1) * Constants.BLOCK_SIZE - Constants.WINDOW_WIDTH);
		} else {
			target_x = target_x + window_center_x;
		}
		
		if (target_y > -window_center_y) {
			target_y = 0;
		} else if (target_y < -((Constants.WORLD_HEIGHT - 1) * Constants.BLOCK_SIZE - Constants.WINDOW_HEIGHT / 2)) {
			target_y = -((Constants.WORLD_HEIGHT - 1) * Constants.BLOCK_SIZE - Constants.WINDOW_HEIGHT);
		} else {
			target_y = target_y + window_center_y;
		}
		
		current_x = target_x;
		current_y = target_y;
		glTranslatef(current_x, current_y, 0);
	}
	
	public int translateX(int x) {
		return x - current_x;
	}
	
	public int translateY(int y) {
		return y - current_y;
	}
}
