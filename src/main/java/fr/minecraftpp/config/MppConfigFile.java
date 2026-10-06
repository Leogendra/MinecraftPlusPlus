package fr.minecraftpp.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import fr.minecraftpp.core.config.MalformedSeedException;
import fr.minecraftpp.core.config.MppSeedParser;

/**
 * The MppConfig.mpp file, which holds the Minecraft++ seed of the game (decision D5: in config/minecraftpp, with the 1.12 format).
 */
public class MppConfigFile
{
	public static final String DIRECTORY_NAME = "minecraftpp";
	public static final String FILE_NAME = "MppConfig.mpp";

	private final Path path;

	public MppConfigFile(Path configDirectory)
	{
		this.path = configDirectory.resolve(DIRECTORY_NAME).resolve(FILE_NAME);
	}

	public Path getPath()
	{
		return this.path;
	}

	/**
	 * Reads the seed, after creating the file with a random seed if it does not exist yet, as in 1.12.
	 */
	public long readOrCreate(Random rand) throws IOException, MalformedSeedException
	{
		if (Files.notExists(this.path))
		{
			Files.createDirectories(this.path.getParent());
			Files.writeString(this.path, MppSeedParser.formatConfig(rand.nextLong()), StandardCharsets.UTF_8);
		}

		return MppSeedParser.parseConfig(Files.readString(this.path, StandardCharsets.UTF_8));
	}
}
