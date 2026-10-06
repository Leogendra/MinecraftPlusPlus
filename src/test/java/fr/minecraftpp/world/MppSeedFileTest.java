package fr.minecraftpp.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import fr.minecraftpp.core.config.MalformedSeedException;
import fr.minecraftpp.core.world.WorldSeedStatus;

class MppSeedFileTest
{
	@TempDir
	Path worldDirectory;

	@Test
	void writesTheSeedInThe112Format() throws IOException, MalformedSeedException
	{
		MppSeedFile file = new MppSeedFile(this.worldDirectory);

		file.write(-42L);

		assertTrue(Files.readString(this.worldDirectory.resolve("mppSeed.mpp")).startsWith("-42\n"));
		assertEquals(Optional.of(-42L), file.read());
	}

	/**
	 * A 1.12 world keeps its status: its file holds the seed on the first line, then a warning.
	 */
	@Test
	void readsA112File() throws IOException
	{
		Files.writeString(this.worldDirectory.resolve("mppSeed.mpp"), "123\nPlease never change this seed, it will break the save. Change the seed in the MppConfig file in your .minecraft folder instead.");

		assertEquals(WorldSeedStatus.VALID, new MppSeedFile(this.worldDirectory).status(123L));
		assertEquals(WorldSeedStatus.WRONG, new MppSeedFile(this.worldDirectory).status(124L));
	}

	@Test
	void aWorldWithoutSeedFileIsVanilla()
	{
		assertEquals(WorldSeedStatus.VANILLA, new MppSeedFile(this.worldDirectory).status(123L));
	}

	@Test
	void anUnreadableSeedIsWrong() throws IOException
	{
		Files.writeString(this.worldDirectory.resolve("mppSeed.mpp"), "not a seed\n");

		assertEquals(WorldSeedStatus.WRONG, new MppSeedFile(this.worldDirectory).status(123L));
	}
}
