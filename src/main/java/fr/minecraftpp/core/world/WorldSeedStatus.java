package fr.minecraftpp.core.world;

import java.util.Optional;

/**
 * Whether a world can be opened with the configured Minecraft++ seed.
 */
public enum WorldSeedStatus
{
	/**
	 * The world was created with the configured seed.
	 */
	VALID,
	/**
	 * The world was created with another seed, or its seed file cannot be read: opening it would change its ores.
	 */
	WRONG,
	/**
	 * The world has no seed file: it was created without the mod.
	 */
	VANILLA;

	/**
	 * @param worldSeed the seed stored in the world, empty when the world has no seed file
	 */
	public static WorldSeedStatus of(Optional<Long> worldSeed, long configuredSeed)
	{
		if (worldSeed.isEmpty())
		{
			return VANILLA;
		}
		else if (worldSeed.get() == configuredSeed)
		{
			return VALID;
		}
		else
		{
			return WRONG;
		}
	}

	public boolean canBeOpened()
	{
		return this == VALID;
	}
}
