package sprawl;

import static org.lwjgl.opengl.GL11.*;

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
	}
	
	public World(PhysicsEngine physics, File load_file) {
		this.physics = physics;
		try {
			load(load_file);
		} catch (IOException e) {
			resetWorld();
		}
	}
	
	public void resetWorld() {
		for (int x = 0; x < Constants.WORLD_WIDTH - 1; x++) {
			for (int y = 0; y < Constants.WORLD_HEIGHT - 1; y++) {
				setAt(x, y, BlockType.AIR);
			}
		}
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
		if (e.physicsType == PhysicsType.DYNAMIC) {
			physics.registerObject(e);
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
	
	public Block blockAt(float x, float y) {
		int indexX = (int)Math.ceil((x / Constants.BLOCK_SIZE)) - 1;
		int indexY = (int)Math.ceil((y / Constants.BLOCK_SIZE)) - 1;
		Block b = null; 
		if (indexX < blocks.length && indexY < blocks[0].length) {
			b = blocks[indexX][indexY];
		}
		return b;
	}
	
	public int draw(Camera camera) {
		//Draw Skybox
		glColor3f(1f, 1f, 1f);
		BlockType.AIR.texture.bind();
		glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(-camera.getX(), -camera.getY());
			glTexCoord2f(1, 0);
			glVertex2f(-camera.getX() + Constants.WINDOW_WIDTH , -camera.getY());
			glTexCoord2f(1, 1);
			glVertex2f(-camera.getX() + Constants.WINDOW_WIDTH, -camera.getY()  + Constants.WINDOW_HEIGHT);
			glTexCoord2f(0, 1);
			glVertex2f(-camera.getX(), -camera.getY()  + Constants.WINDOW_HEIGHT );
	    glEnd();
		
		//Cull unneeded blocks
		int left_edge = camera.leftVisibleBlockIndex();
		int right_edge = camera.rightVisibleBlockIndex();
		int top_edge = camera.topVisibleBlockIndex();
		int bottom_edge = camera.bottomVisibleBlockIndex();

		int tiles_drawn = 0;
		for (int x = left_edge; x < right_edge - 1; x++) {
			for (int y = top_edge; y < bottom_edge - 1; y++) {
				if (blocks[x][y].getType() != BlockType.AIR) {
					blocks[x][y].draw();
					tiles_drawn++;
				}
			}
		}
		return tiles_drawn;
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
	
	public void load(File load_file) throws IOException {
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
		}
	}
	
	public void clear() {
		for (int x = 0; x < Constants.WORLD_WIDTH - 1; x++) {
			for (int y = 0; y < Constants.WORLD_HEIGHT - 1; y++) {
				blocks[x][y] = new Block(BlockType.AIR, x * Constants.BLOCK_SIZE, y * Constants.BLOCK_SIZE);
			}
		}
	}
	
	public int getWidthInPixels() {
		return (blocks.length - 1) * Constants.BLOCK_SIZE;
	}
	
	public int getHeightInPixels() {
		return (blocks[0].length - 1) * Constants.BLOCK_SIZE;
	}
}
