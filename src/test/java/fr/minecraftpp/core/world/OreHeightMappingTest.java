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
	 * Decision D6: a 1.12 maximum height of 16 becomes -48, excluded, as 1.12 excluded the maximum.
	 */
	@Test
	void maximumHeightsAreShiftedDownBy64()
	{
		assertEquals(-49, OreHeightMapping.maxInclusive(new OreGeneration(1, 5, 16)));
		assertEquals(63, OreHeightMapping.maxInclusive(new OreGeneration(1, 5, 128)));
	}

	@Test
	void theLowestOreStaysOnTheBottomLayer()
	{
		assertEquals(-64, OreHeightMapping.maxInclusive(new OreGeneration(1, 5, 1)));
	}
}
