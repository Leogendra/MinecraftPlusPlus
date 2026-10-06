package fr.minecraftpp.core.world;

import fr.minecraftpp.core.set.OreGeneration;

/**
 * Converts the 1.12 ore heights to the current world, whose bottom moved from 0 to -64 (decision D6): every height is shifted down by 64, so the ores keep their place relative to the bottom of the world.
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
		return WORLD_BOTTOM + generation.maxHeight() - 1;
	}
}
