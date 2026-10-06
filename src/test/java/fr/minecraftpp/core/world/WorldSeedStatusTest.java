package fr.minecraftpp.core.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class WorldSeedStatusTest
{
	@Test
	void aWorldWithTheConfiguredSeedIsValid()
	{
		assertEquals(WorldSeedStatus.VALID, WorldSeedStatus.of(Optional.of(42L), 42L));
		assertTrue(WorldSeedStatus.VALID.canBeOpened());
	}

	@Test
	void aWorldWithAnotherSeedIsWrong()
	{
		assertEquals(WorldSeedStatus.WRONG, WorldSeedStatus.of(Optional.of(41L), 42L));
		assertFalse(WorldSeedStatus.WRONG.canBeOpened());
	}

	@Test
	void aWorldWithoutSeedIsVanilla()
	{
		assertEquals(WorldSeedStatus.VANILLA, WorldSeedStatus.of(Optional.empty(), 42L));
		assertFalse(WorldSeedStatus.VANILLA.canBeOpened());
	}
}
