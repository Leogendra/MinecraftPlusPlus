package fr.minecraftpp.core.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MppSeedParserTest
{
	@Test
	void readsTheConfiguredSeed() throws MalformedSeedException
	{
		assertEquals(42L, MppSeedParser.parseConfig("seed=42"));
		assertEquals(-7046029254386353131L, MppSeedParser.parseConfig("seed=-7046029254386353131\r\n"));
	}

	@Test
	void rejectsMalformedConfigurations()
	{
		assertThrows(MalformedSeedException.class, () -> MppSeedParser.parseConfig(""));
		assertThrows(MalformedSeedException.class, () -> MppSeedParser.parseConfig("42"));
		assertThrows(MalformedSeedException.class, () -> MppSeedParser.parseConfig("seed=forty-two"));
		assertThrows(MalformedSeedException.class, () -> MppSeedParser.parseConfig("seed=99999999999999999999"));
	}

	@Test
	void readsTheFirstLineOfAWorldSeedFile() throws MalformedSeedException
	{
		assertEquals(26L, MppSeedParser.parseWorldSeed("26\nPlease never change this seed, it will break the save."));
	}

	@Test
	void rejectsAnEmptyWorldSeedFile()
	{
		assertThrows(MalformedSeedException.class, () -> MppSeedParser.parseWorldSeed(""));
	}

	@Test
	void formattedFilesCanBeReadBack() throws MalformedSeedException
	{
		assertEquals(123L, MppSeedParser.parseConfig(MppSeedParser.formatConfig(123L)));
		assertEquals(-5L, MppSeedParser.parseWorldSeed(MppSeedParser.formatWorldSeed(-5L)));
	}
}
