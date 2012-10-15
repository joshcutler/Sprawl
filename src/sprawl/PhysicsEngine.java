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
	public static final int pixels_per_meter = 16;
	private static final float gravity = 9.81f;
	private static org.jbox2d.dynamics.World physics_world = new org.jbox2d.dynamics.World(new Vec2(0, gravity), true);
	private static Set<ArrayList<Object>> bodies = new HashSet<ArrayList<Object>>();
	
	public PhysicsEngine() {
		physics_world.setContactListener(new FootContactListener());
		
		//Setup left wall
		BodyDef boxDef = new BodyDef();
		boxDef.position.set(0, 0);
		boxDef.type = BodyType.STATIC;
		PolygonShape boxShape = new PolygonShape();
 		boxShape.setAsBox(0, 10000);
		Body left_wall = physics_world.createBody(boxDef);
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = 1;
		boxFixture.shape = boxShape;
		boxFixture.restitution = 0.3f;
		Fixture fix = left_wall.createFixture(boxFixture);
		fix.setUserData("Left Wall");
		
		ArrayList<Object> a = new ArrayList<Object>();
		a.add(left_wall);
		bodies.add(a);
	}
	
	public void update(int delta) {
		physics_world.step(1 / 60f, 10, 5);
		
		for (ArrayList<Object> body_pairs : bodies) {
			Body body = (Body) body_pairs.get(0);
			if (body_pairs.size() == 2) {
				PhysicsEntity o = (PhysicsEntity) body_pairs.get(1);
				Fixture fix = body.getFixtureList();
				while (fix.isSensor()) {
					fix = fix.getNext();
				}
				if (body.getType() == BodyType.DYNAMIC) {
					Vec2 position = body.getPosition().mul(pixels_per_meter);
					PolygonShape shape = (PolygonShape) fix.getShape();
					Vec2 tl = shape.getVertex(0).mul(pixels_per_meter);
					Vec2 tr = shape.getVertex(1).mul(pixels_per_meter);
					Vec2 br = shape.getVertex(2).mul(pixels_per_meter);
					Vec2 bl = shape.getVertex(3).mul(pixels_per_meter);
					float adjusted_x = position.x + tl.x;
					float adjusted_y = position.y + tl.y;
					
					o.setX((int) adjusted_x);
					o.setY((int) adjusted_y);
				}
			}
		}
	}
	
	public void registerObject(PhysicsEntity o) {
		float width = o.getWidth() / (float) pixels_per_meter;
		float height = o.getHeight() / (float) pixels_per_meter;
		
		BodyDef boxDef = new BodyDef();
		boxDef.fixedRotation = true;
		boxDef.position.set((o.getX() / (float) pixels_per_meter) + (width / 2), o.getY() / (float) pixels_per_meter + (height / 2));
		boxDef.type = o.getPhysicsType();
		
		PolygonShape boxShape = new PolygonShape();
 		boxShape.setAsBox(width / 2, height / 2);
		Body body = physics_world.createBody(boxDef);
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = o.getDensity();
		boxFixture.shape = boxShape;
		boxFixture.friction = o.getFriction();
		body.m_mass = o.getMass();
		
		Fixture fix = body.createFixture(boxFixture);
		fix.setUserData(o); 
		body.setUserData(o);
		
		o.setPhysicsBody(body);
		ArrayList<Object> a = new ArrayList<Object>();
		a.add(body);
		a.add(o);
		bodies.add(a);
	}
	
	public void registerEntity(PhysicsEntity o) {
		float width = o.getWidth() / (float) pixels_per_meter;
		float height = o.getHeight() / (float) pixels_per_meter;
		
		BodyDef boxDef = new BodyDef();
		boxDef.fixedRotation = true;
		boxDef.position.set((o.getX() / (float) pixels_per_meter) + (width / 2), o.getY() / (float) pixels_per_meter + (height / 2));
		boxDef.type = o.getPhysicsType();
		
		PolygonShape boxShape = new PolygonShape();
		Vec2[] points = new Vec2[6];
		float stub_length = 0.3f;
		points[0] = new Vec2(-width / 2, -height / 2);
		points[1] = new Vec2(width / 2, -height / 2);
		points[2] = new Vec2(width / 2, height / 2 - stub_length);
		points[3] = new Vec2(width / 2 - stub_length, height / 2);
		points[4] = new Vec2((-width / 2) + stub_length, height / 2);
		points[5] = new Vec2((-width / 2), (height / 2));
		boxShape.set(points, 6);
 		Body body = physics_world.createBody(boxDef);
		FixtureDef boxFixture = new FixtureDef();
		boxFixture.density = o.getDensity();
		boxFixture.shape = boxShape;
		boxFixture.friction = o.getFriction();
		body.m_mass = o.getMass();
		
		Fixture fix = body.createFixture(boxFixture);
		fix.setUserData(o); 
		body.setUserData(o);
		
		o.setPhysicsBody(body);
		ArrayList<Object> a = new ArrayList<Object>();
		a.add(body);
		a.add(o);
		bodies.add(a);
	}
	
	public void registerFootSensor(PhysicsEntity o) {
		PolygonShape sensorShape = new PolygonShape();
		float sensor_width = o.getWidth() / (float) pixels_per_meter;
		float sensor_height = 0.15f;
		sensorShape.setAsBox(sensor_width / 2, sensor_height / 2, new Vec2(0, (o.getHeight() / (float) pixels_per_meter / 2)), 0);
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
			
			RenderingEngine.drawPhysicsEntity(b);
		}glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}
}
