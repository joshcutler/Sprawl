package sprawl;

import java.util.ArrayList;

import sprawl.entities.PC;
import sprawl.items.Item;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

public class HUD {
	public static Nifty nifty;
	public static Screen screen;
	private static boolean HUDDrawn = false;
	
	public static void toggleInventory() {
	    if (!HUD.HUDDrawn) {
	    	HUD.drawInventory();
	    	HUD.HUDDrawn = true;
	    }
	    
		Element inventory = nifty.getCurrentScreen().findElementByName("inventory");
		inventory.setVisible(!inventory.isVisible());
	}
	
	public static void selectItem(Element slot, String itemHash) {
		//TODO: Do something in the UI to show that it is selected
		
		System.out.println("Searching for Item: " + itemHash);
		Item item = Game.currentGame.getPC().getItemByHash(itemHash);
		if (item != null) {
			System.out.println("Item Selected: " + item.toString());
			Game.selected_item = item.getHash();
		}
	}
	
	public static void drawInventory() {
		Game game = Game.currentGame;
		PC pc = game.getPC();
		int row_length = 16;
		ArrayList<Item> inventory  = pc.getInventory();
		
		Element inventory_element = nifty.getCurrentScreen().findElementByName("inventory");
		for (Element el : inventory_element.getElements()) {
			el.markForRemoval(null);
		}
		
		// Draw inventory slots
		for (int j = 1; j <= pc.getInventorySize() / row_length; j++) {
			boolean lastRow = false;
			Element p = new PanelBuilder(){{
				width("100%");
				childLayoutHorizontal();
			}}.build(nifty, screen, inventory_element);
			
			int slots_to_draw = row_length;
			if (j * row_length > pc.getInventorySize()) {
				slots_to_draw = pc.getInventorySize() % row_length;
			}
			if (j * row_length >= pc.getInventorySize()) {
				lastRow = true;
			}
			for (int i = 0; i < slots_to_draw; i++) {
				Element el = null;
				final String slotId = "slot" + ((j-1)*row_length + i);
				try {
					final Item item = inventory.get((j-1)*row_length  + i);
					final String texture = item.getType().texture_location.substring(1);
					
					el = new ControlBuilder(slotId, "droppable") {{
						width("32px");
						height("32px");
						marginTop("4px");
						marginLeft("4px");
						panel(new PanelBuilder() {{
							backgroundColor("#3333");
							childLayoutCenter();
							interactOnClick("selectItem(" + slotId + ")");
							control(new DraggableBuilder("item-" + item.getHash()) {{
								childLayoutAbsolute();
								image(new ImageBuilder() {{
									childLayoutCenter();
									filename(texture);
									x("0px");
									y("0px");
								}});
								if (item.getQuantity() > 1) {
									text(new TextBuilder() {{
										text(String.valueOf(item.getQuantity()));
										style("hud-font-10");
										x("16px");
										y("22px");
									}});
								}
							}});
						}});
					}}.build(nifty, screen, p); 
					
				} catch (IndexOutOfBoundsException e) {
					el = new ControlBuilder(slotId, "droppable") {{
						width("32px");
						height("32px");
						marginTop("4px");
						marginLeft("4px");
						panel(new PanelBuilder() {{
							backgroundColor("#3333");
							childLayoutCenter();
							interactOnClick("selectItem(" + slotId + ")");
						}});
					}}.build(nifty, screen, p); 
				}
				
				//Special Case padding on the end
				if (i == slots_to_draw - 1) {
					el.setMarginRight(new SizeValue("4px"));
				}
				if (lastRow) {
					el.setMarginBottom(new SizeValue("4px"));
				}
			}
		}
	}
}
