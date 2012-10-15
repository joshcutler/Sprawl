package sprawl;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

public class FootContactListener implements ContactListener {

	@Override
	public void beginContact(Contact c) {
		System.out.println("Collision: " + c.getFixtureA().getUserData() + " " + c.getFixtureB().getUserData());
		if (c.getFixtureB().getUserData() == "Foot Sensor") {
			((Entity)c.getFixtureB().getBody().getUserData()).incrementFootContacts();
			//Stop vertical movement
			Vec2 current_velocity = c.getFixtureB().getBody().getLinearVelocity();
			c.getFixtureB().getBody().setLinearVelocity(new Vec2(current_velocity.x, 0));
		}
	}

	@Override
	public void endContact(Contact c) {
		if (c.getFixtureB().getUserData() == "Foot Sensor") {
			((PC)c.getFixtureB().getBody().getUserData()).decrementFootContacts();
		}
	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {
		// TODO Auto-generated method stub

	}

}
