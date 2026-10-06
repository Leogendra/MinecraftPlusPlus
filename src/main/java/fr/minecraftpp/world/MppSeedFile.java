package fr.minecraftpp.world;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import fr.minecraftpp.core.config.MalformedSeedException;
import fr.minecraftpp.core.config.MppSeedParser;
import fr.minecraftpp.core.world.WorldSeedStatus;

/**
 * The {@code mppSeed.mpp} file of a world: the Minecraft++ seed the world was created with, in the 1.12 format, so that 1.12 worlds keep their status.
 */
public class MppSeedFile
{
	public static final String FILE_NAME = "mppSeed.mpp";

	private final Path path;

	public MppSeedFile(Path worldDirectory)
	{
		this.path = worldDirectory.resolve(FILE_NAME);
	}

	public Path getPath()
	{
		return this.path;
	}

	/**
	 * The seed of the world, empty when the world has no seed file.
	 */
	public Optional<Long> read() throws IOException, MalformedSeedException
	{
		if (Files.notExists(this.path))
		{
			return Optional.empty();
		}
		else
		{
			return Optional.of(MppSeedParser.parseWorldSeed(Files.readString(this.path, StandardCharsets.UTF_8)));
		}
	}

	public void write(long seed) throws IOException
	{
		Files.createDirectories(this.path.getParent());
		Files.writeString(this.path, MppSeedParser.formatWorldSeed(seed), StandardCharsets.UTF_8);
	}

	/**
	 * Whether the world can be opened with the configured seed. A seed file that cannot be read gives {@link WorldSeedStatus#WRONG}: the seed the world needs is unknown.
	 */
	public WorldSeedStatus status(long configuredSeed)
	{
		try
		{
			return WorldSeedStatus.of(this.read(), configuredSeed);
		}
		catch (IOException | MalformedSeedException exception)
		{
			return WorldSeedStatus.WRONG;
		}
	}
}
