package fr.minecraftpp.core.world;

import fr.minecraftpp.core.set.OreGeneration;

/**
 * Converts the 1.12 ore heights to the current world, whose bottom moved from 0 to -64 (decision D6): a vein keeps its 1.12 maximum height and reaches down to the new bottom of the world, so every ore also generates above the deepslate. The number of veins grows with the height range, so that each layer keeps the 1.12 density.
 */
public final class OreHeightMapping
{
	public static final int WORLD_BOTTOM = -64;

	private OreHeightMapping()
	{
	}

	public static int minInclusive()
	{
		return WORLD_BOTTOM;
	}

	/**
	 * 1.12 drew the height of a vein from 0 to the maximum height excluded.
	 */
	public static int maxInclusive(OreGeneration generation)
	{
		return generation.maxHeight() - 1;
	}

	/**
	 * The 1.12 veins per chunk, scaled by the growth of the height range and rounded to the nearest.
	 */
	public static int veinsPerChunk(OreGeneration generation)
	{
		int range = generation.maxHeight() - WORLD_BOTTOM;

		return (generation.veinAmount() * range + generation.maxHeight() / 2) / generation.maxHeight();
	}
}
