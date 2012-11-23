package sprawl.controllers;

import java.util.List;

import sprawl.HUD;
import de.lessvoid.nifty.EndNotify;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DroppableDroppedEvent;
import de.lessvoid.nifty.effects.EffectEventId;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class HUDController implements ScreenController {
	private Nifty nifty;
	private Screen screen;
	
	@Override
	public void bind(Nifty niftyParam, Screen screenParam) {
		this.nifty = niftyParam;
	    this.screen = screenParam;
	    
	    HUD.nifty = nifty;
	    HUD.screen = screen;
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub
		
	}
	
	public void toggleInventory() {
		HUD.toggleInventory();
	}
	
	public void selectItem(String slotId) {
		Element slot = screen.findElementByName(slotId);
		
		List<Element> children = slot.getElements().get(0).getElements();
		for (Element e  : children) {
			if (e.getId() != null) {
				HUD.selectItem(slot, e.getId().substring(5));
			}
		}
	}
	
	@NiftyEventSubscriber(pattern="slot.*")
	public void onDrop(final String id, final DroppableDroppedEvent event) {
		System.out.println("Dropped: " + event.getDraggable().getId());
		HUD.selectItem(null, event.getDraggable().getId().substring(5));
	}
}
