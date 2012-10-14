package sprawl;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;

public interface PhysicsEntity {
	public void setPhysicsBody(Body b);
	public Body getPhysicsBody();
	public void move(Vec2 force, Vec2 position);
	
	public int getX();
	public int getY();
	public void setX(int x);
	public void setY(int y);
	public int getWidth();
	public int getHeight();
	public float getFriction();
	public BodyType getPhysicsType();
	public float getDensity();
}
