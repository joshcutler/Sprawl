package sprawl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

public class World {
	private Block[][] blocks = new Block[Constants.WORLD_WIDTH][Constants.WORLD_HEIGHT];
	private List<Entity> entities = new ArrayList<Entity>();
	private PhysicsEngine physics;
	
	public World(PhysicsEngine physics) {
		this.physics = physics;
		for (int x = 0; x < Constants.WORLD_WIDTH - 1; x++) {
			for (int y = 0; y < Constants.WORLD_HEIGHT - 1; y++) {
				setAt(x, y, BlockType.AIR);
			}
		}
	}
	
	public World(PhysicsEngine physics, File load_file) {
		this.physics = physics;
		load(load_file);
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
		if (e.hasPhysics) {
			physics.registerEntity(e);
			e.registerSensors(physics);
		}
	}
	
	public List<Entity> getEntities() {
		return this.entities;
	}
	
	public void setAt(int x, int y, BlockType b) {
		Block block = new Block(b, x * Constants.BLOCK_SIZE, y * Constants.BLOCK_SIZE);
		blocks[x][y] = block;
		if (b.has_physics) {
			physics.registerObject(block);
		}
	}
	
	public Block getAt(int x, int y) {
		if (x < Constants.WORLD_WIDTH && y < Constants.WORLD_HEIGHT) {
			return blocks[x][y];
		}
		return null;
	}
	
	public void draw() {
		//TODO Add frustrum culling 
		int left_edge = 0;
		int right_edge = 0;
		for (int x = left_edge; x < Constants.WORLD_WIDTH - 1; x++) {
			for (int y = 0; y < Constants.WORLD_HEIGHT - 1; y++) {
				blocks[x][y].draw();
			}
		}
	}
		
	public void save(File save_file) {
		Document document = new Document();
		Element root = new Element("blocks");
		document.setRootElement(root);
		
		for (int x = 0; x < Constants.WORLD_WIDTH - 1; x++) {
			for (int y = 0; y < Constants.WORLD_HEIGHT - 1; y++) {
				Element block = new Element("block");
				block.setAttribute("x", String.valueOf(x));
				block.setAttribute("y", String.valueOf(y));
				block.setAttribute("type", String.valueOf(blocks[x][y].getType()));
				root.addContent(block);
			}
		}
		XMLOutputter output = new XMLOutputter();
		try {
			output.output(document, new FileOutputStream(save_file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void load(File load_file) {
		try {
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(load_file);
			Element root = document.getRootElement();
			for (Element block : root.getChildren()) {
				int x = Integer.parseInt(block.getAttributeValue("x"));
				int y = Integer.parseInt(block.getAttributeValue("y"));
				this.setAt(x, y, BlockType.valueOf(block.getAttributeValue("type")));
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clear() {
		for (int x = 0; x < Constants.WORLD_WIDTH - 1; x++) {
			for (int y = 0; y < Constants.WORLD_HEIGHT - 1; y++) {
				blocks[x][y] = new Block(BlockType.AIR, x * Constants.BLOCK_SIZE, y * Constants.BLOCK_SIZE);
			}
		}
	}
}
