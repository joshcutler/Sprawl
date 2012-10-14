package sprawl;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class FootContactListener implements ContactListener {

	@Override
	public void beginContact(Contact c) {
		if (c.getFixtureB().getUserData() == "Foot Sensor") {
			((Entity)c.getFixtureB().getBody().getUserData()).incrementFootContacts();
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
