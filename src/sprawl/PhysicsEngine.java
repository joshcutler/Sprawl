package sprawl;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.glPolygonMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

import org.lwjgl.opengl.GL11.*;

public class PhysicsEngine {
	private static final int pixels_per_meter = 32;
	private static final float gravity = 9.81f;
	private static org.jbox2d.dynamics.World physics_world = new org.jbox2d.dynamics.World(new Vec2(0, gravity), true);
	private static Set<ArrayList<Object>> bodies = new HashSet<ArrayList<Object>>();
	
	public PhysicsEngine() {
		physics_world.setContactListener(new FootContactListener());
	}
	
	public void update(int delta) {
		physics_world.step(1/(float) delta * .02f, 8, 3);
		
		for (ArrayList<Object> body_pairs : bodies) {
			Body body = (Body) body_pairs.get(0);
			PhysicsEntity o = (PhysicsEntity) body_pairs.get(1);
			if (body.getType() == BodyType.DYNAMIC) {
				Vec2 position = body.getPosition().mul(pixels_per_meter);
				o.setX((int) position.x);
				o.setY((int) position.y);
			}
		}
	}
	
	public void registerObject(PhysicsEntity o) {
		BodyDef boxDef = new BodyDef();
		boxDef.position.set(o.getX() / (float)pixels_per_meter, o.getY() / (float)pixels_per_meter);
		boxDef.type = o.getPhysicsType();
		
		PolygonShape boxShape = new PolygonShape();
 		boxShape.setAsBox(o.getWidth() / (float)pixels_per_meter, o.getHeight() / (float)pixels_per_meter);
		Body body = physics_world.createBody(boxDef);
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = o.getDensity();
		boxFixture.shape = boxShape;
		boxFixture.friction = o.getFriction();
		body.createFixture(boxFixture);
		
		o.setPhysicsBody(body);
		body.setUserData(o); 
		ArrayList<Object> a = new ArrayList<Object>();
		a.add(body);
		a.add(o);
		bodies.add(a);
	}
	
	public void registerFootSensor(PhysicsEntity o) {
		PolygonShape sensorShape = new PolygonShape();
 		sensorShape.setAsBox(o.getWidth() / (float)pixels_per_meter, 0.3f);
 		FixtureDef sensorFixtureDef = new FixtureDef();
 		sensorFixtureDef.shape = sensorShape;
 		sensorFixtureDef.density = 0;
 		sensorFixtureDef.isSensor = true;
 		
 		Fixture sensorFixture = o.getPhysicsBody().createFixture(sensorFixtureDef);
 		sensorFixture.setUserData("Foot Sensor");
	}
	
	public void drawPhysicsWorld() {
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		for (ArrayList<Object> a : bodies) {
			Body b = (Body) a.get(0);
			PhysicsEntity p = (PhysicsEntity) a.get(1);
			
			RenderingEngine.drawPhysicsEntity(p);
		}glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}
}
