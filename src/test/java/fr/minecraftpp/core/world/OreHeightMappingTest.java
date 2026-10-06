package fr.minecraftpp.core.world;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.set.OreGeneration;

class OreHeightMappingTest
{
	@Test
	void veinsStartAtTheBottomOfTheWorld()
	{
		assertEquals(-64, OreHeightMapping.minInclusive());
	}

	/**
	 * Decision D6: the 1.12 maximum height is kept, excluded as 1.12 excluded it, so that every ore also generates above the deepslate.
	 */
	@Test
	void maximumHeightsAreKept()
	{
		assertEquals(15, OreHeightMapping.maxInclusive(new OreGeneration(1, 5, 16)));
		assertEquals(127, OreHeightMapping.maxInclusive(new OreGeneration(1, 5, 128)));
	}

	@Test
	void everyOreReachesAboveTheDeepslate()
	{
		assertEquals(0, OreHeightMapping.maxInclusive(new OreGeneration(1, 5, 1)));
	}

	@Test
	void veinsPerChunkGrowWithTheHeightRange()
	{
		assertEquals(5, OreHeightMapping.veinsPerChunk(new OreGeneration(1, 5, 16)));
		assertEquals(6, OreHeightMapping.veinsPerChunk(new OreGeneration(2, 5, 32)));
		assertEquals(20, OreHeightMapping.veinsPerChunk(new OreGeneration(10, 5, 64)));
		assertEquals(5, OreHeightMapping.veinsPerChunk(new OreGeneration(3, 5, 128)));
	}
}
