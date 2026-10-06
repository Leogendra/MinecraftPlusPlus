package fr.minecraftpp.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import fr.minecraftpp.core.naming.NameGenerator;

/**
 * The name dictionary, embedded in the mod resources (decision D5).
 */
public final class WordGenDictionary
{
	public static final String RESOURCE = "/minecraftpp/wordGen.mpp";

	private WordGenDictionary()
	{
	}

	public static NameGenerator nameGenerator(long seed) throws IOException
	{
		try (InputStream stream = WordGenDictionary.class.getResourceAsStream(RESOURCE))
		{
			if (stream == null)
			{
				throw new IOException("The name dictionary " + RESOURCE + " is missing from the mod jar");
			}

			return new NameGenerator(seed, new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)));
		}
	}
}
