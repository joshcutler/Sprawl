package sprawl;

import static org.lwjgl.opengl.GL11.glTranslatef;
import sprawl.entities.PC;

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
	
	public int leftVisibleBlockIndex() {
		int left_edge = (int) -Math.floor((this.getX() / Constants.BLOCK_SIZE)) - 1;
		if (left_edge < 0) {
			left_edge = 0;
		}
		return left_edge;
	}

	public int rightVisibleBlockIndex() {
		int right_edge = (int) Math.ceil(((-this.getX() + Constants.WINDOW_WIDTH) / Constants.BLOCK_SIZE)) + 2;
		if (right_edge >= Constants.WORLD_WIDTH) {
			right_edge = Constants.WORLD_WIDTH;
		}
		return right_edge;
	}
	public int topVisibleBlockIndex() {
		int top_edge = (int) -Math.floor((this.getY() / Constants.BLOCK_SIZE)) - 1;
		if (top_edge < 0) {
			top_edge = 0;
		}
		return top_edge;
	}
	public int bottomVisibleBlockIndex() {
		int bottom_edge = (int) Math.ceil(((-this.getY() + Constants.WINDOW_HEIGHT) / Constants.BLOCK_SIZE)) + 2;
		if (bottom_edge >= Constants.WORLD_HEIGHT) {
			bottom_edge = Constants.WORLD_HEIGHT;
		}
		return bottom_edge;
	}
}
