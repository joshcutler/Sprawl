package sprawl;

import sprawl.entities.PC;
import sprawl.items.Item;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.Draggable;
import de.lessvoid.nifty.controls.Droppable;
import de.lessvoid.nifty.controls.DroppableDropFilter;
import de.lessvoid.nifty.controls.dragndrop.builder.DraggableBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.SizeValue;

public class HUD {
	public static Nifty nifty;
	public static Screen screen;
	private static boolean HUDDrawn = false;
	
	public static void reset() {
		HUD.HUDDrawn = false;
	}
	
	public static void toggleInventory() {
	    if (!HUD.HUDDrawn) {
	    	HUD.drawInventory();
	    	HUD.HUDDrawn = true;
	    }
	    
		Element inventory = nifty.getCurrentScreen().findElementByName("inventory");
		inventory.setVisible(!inventory.isVisible());
	}
	
	public static String reorderItems(String slotId, String itemHash) {
		Game game = Game.currentGame;
		PC pc = game.getPC();
		
		Item oldItem = null;
		Item newItem = pc.getItemByHash(itemHash);
		
		pc.removeItem(itemHash);
		pc.setItemAt(Integer.parseInt(slotId), newItem);
		
		return (oldItem != null) ? oldItem.getHash() : null;
	}
	
	public static void selectItem(Element slot, String itemHash) {
		for (int i = 0; i < Game.currentGame.getPC().getInventorySize(); i++) {
			screen.findElementByName("slot" + i).setStyle("inventoryTile");
		}
		slot.setStyle("inventoryTileSelected");
		
		System.out.println("Searching for Item: " + itemHash);
		Item item = Game.currentGame.getPC().getItemByHash(itemHash);
		if (item != null) {
			System.out.println("Item Selected: " + item.toString());
			Game.selected_item = item.getHash();
		}
		drawInventory();
	}
	
	public static void drawInventory() {
		Game game = Game.currentGame;
		PC pc = game.getPC();
		int row_length = 16;
		Item[] inventory  = pc.getInventory();
		
		Element inventory_element = nifty.getCurrentScreen().findElementByName("inventory");
		for (Element el : inventory_element.getElements()) {
			el.markForRemoval();
		}
		
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
				final Item item = inventory[(j - 1)*row_length  + i];
				
				el = new PanelBuilder(slotId) {{
					style("inventoryTileWrapper");
					panel(new PanelBuilder() {{
						if (item != null && item.getHash().equals(Game.selected_item)) {
							style("inventoryTileSelected");
						} else {
							style("inventoryTile");
						}
						if (item != null) {
							final String texture = item.getType().texture_location.substring(1);
							interactOnClick("selectItem(" + slotId + ")");
							interactOnClickMouseMove("beginDragCheck()");
							panel(new PanelBuilder("item-" + item.getHash()) {{
								childLayoutAbsolute();
								image(new ImageBuilder() {{
									filename(texture);
									childLayoutCenter();
									x("0px");
									y("0px");
								}});
								if (item.getQuantity() > 1) {
									text(new TextBuilder() {{
										text(String.valueOf(item.getQuantity()));
										style("stackCount");
									}});
								}
							}});
						}
					}});
				}}.build(nifty, screen, p); 
				
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
