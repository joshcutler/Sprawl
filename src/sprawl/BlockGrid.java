package sprawl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import sprawl.Constants;

public class BlockGrid {
	private Block[][] blocks = new Block[Constants.GRID_WIDTH][Constants.GRID_HEIGHT];
	
	public BlockGrid() {
		for (int x = 0; x < Constants.GRID_WIDTH - 1; x++) {
			for (int y = 0; y < Constants.GRID_HEIGHT - 1; y++) {
				blocks[x][y] = new Block(BlockType.AIR, x * Constants.BLOCK_SIZE, y * Constants.BLOCK_SIZE);
			}
		}
	}
	
	public void setAt(int x, int y, BlockType b) {
		blocks[x][y] = new Block(b, x * Constants.BLOCK_SIZE, y * Constants.BLOCK_SIZE);
	}
	
	public Block getAt(int x, int y) {
		return blocks[x][y];
	}
	
	public void draw() {
		for (int x = 0; x < Constants.GRID_WIDTH - 1; x++) {
			for (int y = 0; y < Constants.GRID_HEIGHT - 1; y++) {
				blocks[x][y].draw();
			}
		}
	}
	
	public BlockGrid(File load_ile) {
		
	}
	
	public void save(File save_file) {
		Document document = new Document();
		Element root = new Element("blocks");
		document.setRootElement(root);
		
		for (int x = 0; x < Constants.GRID_WIDTH - 1; x++) {
			for (int y = 0; y < Constants.GRID_HEIGHT - 1; y++) {
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
				blocks[x][y] = new Block(BlockType.valueOf(block.getAttributeValue("type")), x * Constants.BLOCK_SIZE, y * Constants.BLOCK_SIZE);
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
		for (int x = 0; x < Constants.GRID_WIDTH - 1; x++) {
			for (int y = 0; y < Constants.GRID_HEIGHT - 1; y++) {
				blocks[x][y] = new Block(BlockType.AIR, x * Constants.BLOCK_SIZE, y * Constants.BLOCK_SIZE);
			}
		}
	}
}
