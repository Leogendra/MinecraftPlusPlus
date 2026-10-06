package fr.minecraftpp.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import fr.minecraftpp.core.config.MalformedSeedException;

class MppConfigFileTest
{
	@TempDir
	Path configDirectory;

	@Test
	void createsTheFileWithARandomSeedOnFirstLaunch() throws IOException, MalformedSeedException
	{
		MppConfigFile file = new MppConfigFile(this.configDirectory);

		long seed = file.readOrCreate(new Random(7));

		assertEquals(new Random(7).nextLong(), seed);
		assertTrue(Files.readString(this.configDirectory.resolve("minecraftpp").resolve("MppConfig.mpp")).startsWith("seed=" + seed));
	}

	@Test
	void keepsTheSeedOfAnExistingFile() throws IOException, MalformedSeedException
	{
		write("seed=42\n");

		assertEquals(42L, new MppConfigFile(this.configDirectory).readOrCreate(new Random()));
	}

	@Test
	void reportsAMalformedFile() throws IOException
	{
		write("seed: 42\n");

		assertThrows(MalformedSeedException.class, () -> new MppConfigFile(this.configDirectory).readOrCreate(new Random()));
	}

	private void write(String content) throws IOException
	{
		Path directory = Files.createDirectories(this.configDirectory.resolve("minecraftpp"));
		Files.writeString(directory.resolve("MppConfig.mpp"), content);
	}
}
