package fr.minecraftpp.core.config;

/**
 * Reads and writes the two seed files of the mod, with the 1.12 formats:
 * <ul>
 * <li>MppConfig.mpp, the configured seed: {@code seed=<number>} on the first line;</li>
 * <li>mppSeed.mpp, the seed a world was created with: the number on the first line, then a warning for the player.</li>
 * </ul>
 */
public final class MppSeedParser
{
	private static final String CONFIG_PREFIX = "seed=";
	private static final String WORLD_SEED_WARNING = "Please never change this seed, it will break the save. Change the seed in the MppConfig file in your config/minecraftpp folder instead.";

	private MppSeedParser()
	{
	}

	public static long parseConfig(String content) throws MalformedSeedException
	{
		String firstLine = firstLine(content);

		if (!firstLine.startsWith(CONFIG_PREFIX))
		{
			throw new MalformedSeedException("The first line must be \"" + CONFIG_PREFIX + "<number>\", found \"" + firstLine + "\"");
		}
		else
		{
			return parseNumber(firstLine.substring(CONFIG_PREFIX.length()));
		}
	}

	public static String formatConfig(long seed)
	{
		return CONFIG_PREFIX + seed + "\n";
	}

	public static long parseWorldSeed(String content) throws MalformedSeedException
	{
		return parseNumber(firstLine(content));
	}

	public static String formatWorldSeed(long seed)
	{
		return seed + "\n" + WORLD_SEED_WARNING + "\n";
	}

	private static String firstLine(String content)
	{
		return content.lines().findFirst().orElse("").trim();
	}

	private static long parseNumber(String text) throws MalformedSeedException
	{
		try
		{
			return Long.parseLong(text.trim());
		}
		catch (NumberFormatException exception)
		{
			throw new MalformedSeedException("\"" + text + "\" is not a seed: a seed is a whole number between " + Long.MIN_VALUE + " and " + Long.MAX_VALUE);
		}
	}
}
