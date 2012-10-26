package sprawl.world;

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

import sprawl.Camera;
import sprawl.Constants;
import sprawl.Vec2;
import sprawl.entities.Entity;
import sprawl.vegetation.CoverType;
import sprawl.vegetation.Vegetation;

public class World {
	private Block[][] blocks = new Block[Constants.WORLD_WIDTH][Constants.WORLD_HEIGHT];
	private List<Entity> entities = new ArrayList<Entity>();
	private int seaLevel, crustLevel;
	private List<Biome> biomes = new ArrayList<Biome>();
	
	public World() {}
	
	public World(File load_file) {
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
	}
	
	public List<Entity> getEntities() {
		return this.entities;
	}
	
	public void setAt(int x, int y, BlockType b) {
		Block block = new Block(b);
		blocks[x][y] = block;
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
	
	public Vec2 blockStartsAt(float x, float y) {
		return(new Vec2(
				(int) (Math.ceil((x / Constants.BLOCK_SIZE) - 1) * Constants.BLOCK_SIZE), 
				(int) (Math.ceil((y / Constants.BLOCK_SIZE) - 1) * Constants.BLOCK_SIZE)
			));
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
					Vec2 pos = World.getBlockCoordinates(x, y);
					blocks[x][y].draw(pos.x, pos.y);
					Vegetation vegetation = blocks[x][y].getVegetation();
					if (vegetation != null) {
						vegetation.draw((int)pos.x, (int)pos.y);
					}
					
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
				blocks[x][y] = new Block(BlockType.AIR);
			}
		}
	}
	
	public int getWidthInPixels() {
		return (blocks.length - 1) * Constants.BLOCK_SIZE;
	}
	
	public int getHeightInPixels() {
		return (blocks[0].length - 1) * Constants.BLOCK_SIZE;
	}
	
	public void growPlants(int delta, Camera camera) {
		int left_edge = camera.leftVisibleBlockIndex();
		int right_edge = camera.rightVisibleBlockIndex();
		int top_edge = camera.topVisibleBlockIndex();
		int bottom_edge = camera.bottomVisibleBlockIndex();

		for (int x = left_edge; x < right_edge - 1; x++) {
			for (int y = top_edge; y < bottom_edge - 1; y++) {
				if (blocks[x][y].getType() != BlockType.AIR) {
					Vegetation vegetation = blocks[x][y].getVegetation();
					if (vegetation != null) {
						if (Math.random() < (vegetation.getGrowthRate() * delta / 1000f)) {
							vegetation.grow();
						}
					}
				}
			}
		}
	}
	
	public void growGroundCover(int delta, Camera camera) {
		int left_edge = camera.leftVisibleBlockIndex();
		int right_edge = camera.rightVisibleBlockIndex();
		int top_edge = camera.topVisibleBlockIndex();
		int bottom_edge = camera.bottomVisibleBlockIndex();

		for (int x = left_edge; x < right_edge - 1; x++) {
			for (int y = top_edge; y < bottom_edge - 1; y++) {
				if (blocks[x][y].getType() != BlockType.AIR) {
					CoverType cover = blocks[x][y].getCoverType();
					if (cover != null) {
						if (Math.random() < (cover.growthRate * delta / 1000f)) {
							
							//Grow Left
							if (x - 1 >= 0) {
								Block left = blocks[x-1][y];
								Block left_top = blocks[x-1][y-1];
								
								if (left.getType() == BlockType.AIR) {
									//TODO: Draw side grass down!
								} else if (left_top.getType() == BlockType.DIRT) {
									//TODO: Need to grow side grass up!
								} else if (left.getType() == BlockType.DIRT && left_top.getType() == BlockType.AIR) {
									if (left.getCoverType() == null) {
										left.setCoverType(cover);
									}
								}
							}
							
							//Grow right
							if (x + 1 < Constants.WINDOW_WIDTH) {
								Block right = blocks[x+1][y];
								Block right_top = blocks[x+1][y-1];
								
								if (right.getType() == BlockType.AIR) {
									//TODO: Draw side grass down!
								} else if (right_top.getType() == BlockType.DIRT) {
									//TODO: Need to grow side grass up!
								} else if (right.getType() == BlockType.DIRT && right_top.getType() == BlockType.AIR) {
									if (right.getCoverType() == null) {
										right.setCoverType(cover);
									}
								}
							}
							
						}
					}
				}
			}
		}
	}
	
	public static Vec2 getBlockCoordinates(int x, int y) {
		return(new Vec2(x * Constants.BLOCK_SIZE, y * Constants.BLOCK_SIZE));
	}

	public int getSeaLevel() {
		return seaLevel;
	}

	public void setSeaLevel(int seaLevel) {
		this.seaLevel = seaLevel;
	}

	public int getCrustLevel() {
		return crustLevel;
	}

	public void setCrustLevel(int crustLevel) {
		this.crustLevel = crustLevel;
	}

	public List<Biome> getBiomes() {
		return biomes;
	}

	public void setBiomes(List<Biome> biomes) {
		this.biomes = biomes;
	}
	
	public Biome biomeAt(int x) {
		int count = 0;
		for (Biome b: this.biomes) {
			if (x >= count && x < count + b.getWidth()) {
				return b;
			}
			count += b.getWidth();
		}
		
		return null;
	}
}
