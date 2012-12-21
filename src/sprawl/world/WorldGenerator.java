package sprawl.world;

import java.util.ArrayList;
import java.util.Random;

import sprawl.Constants;
import sprawl.PhysicsEngine;
import sprawl.vegetation.CoverType;
import sprawl.vegetation.Tree;

public class WorldGenerator {
	public static World generate(int seed, PhysicsEngine physics) {
		Random gen = new Random(seed);
		World world = new World(physics);
		
		int seaLevel = (int) ((int) Constants.WORLD_HEIGHT * 0.10);
		int crustLevel = (int) ((int) Constants.WORLD_HEIGHT * 0.10) + seaLevel;
		int currentElevation = seaLevel;
		
		//Generate the Biomes
		ArrayList<Biome> biomes = WorldGenerator.generateBiomes(gen);
		world.setBiomes(biomes);
		
		// Generate the land based on this biome
		for (int i = 0; i < Constants.WORLD_WIDTH; i++) {
			Biome biome = world.biomeAt(i);
			BiomeType biomeType = biome.getType();
			
			// Calculate elevation change
			float changeElevation = gen.nextFloat();
			if (changeElevation <= biomeType.elevationUp) {
				currentElevation += gen.nextInt(biomeType.maxElevationChange + 1);
			} else if (changeElevation <= biomeType.elevationUp + biomeType.elevationDown) {
				currentElevation -= gen.nextInt(biomeType.maxElevationChange + 1);
			}
			
			for (int j = 0; j < Constants.WORLD_HEIGHT; j++) {
				// Generate Blocks in a vertical strip
				if (j < currentElevation) {
					world.setAt(i, j, BlockType.AIR);
				} else if (j >= currentElevation && j < crustLevel) {
					world.setAt(i, j, BlockType.DIRT);
				} else if (j > crustLevel) {
					world.setAt(i, j, BlockType.STONE);
				}
				
				// Generate Vegetation
				if (j == currentElevation) {
					Block b = world.getAt(i, j);
					b.setCoverType(CoverType.GRASS);
					if (gen.nextFloat() < biomeType.treeGrowth) {
						b.setVegetation(new Tree(gen.nextInt(Tree.maxHeight)));
						b.getVegetation().updateForegroundBlocks(i, j, world);
					}
				}
			}
		}
		return world;
	}
	
	public static ArrayList<Biome> generateBiomes(Random gen) {
		ArrayList<Biome> biomes = new ArrayList<Biome>();
		
		int biomeCover = 0;
		BiomeType[] biomeTypes = BiomeType.values();
		while (biomeCover < Constants.WORLD_WIDTH) {
			//Pick a biome
			BiomeType type = biomeTypes[gen.nextInt(biomeTypes.length)];
			//Set its size
			int size = BiomeType.biomeWidth + (gen.nextInt(BiomeType.biomeVariance) * (-1 ^ (gen.nextInt(2) + 1)));
			
			biomes.add(new Biome(type, size));
			biomeCover += size;
		}
		
		return biomes;
	}
}
