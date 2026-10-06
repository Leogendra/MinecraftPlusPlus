package fr.minecraftpp.core.ore;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RarityTest
{
	@Test
	void nextGoesUpOneLevel()
	{
		assertEquals(Rarity.FAMILIAR, Rarity.COMMON.next());
		assertEquals(Rarity.EPIC, Rarity.RARE.next());
	}

	@Test
	void nextStopsAtLegendary()
	{
		assertEquals(Rarity.LEGENDARY, Rarity.EPIC.next());
		assertEquals(Rarity.LEGENDARY, Rarity.LEGENDARY.next());
	}

	@Test
	void keepsThe112DisplayNames()
	{
		assertEquals("Familiar", Rarity.FAMILIAR.getDisplayName());
		assertEquals("Legendary", Rarity.LEGENDARY.getDisplayName());
	}
}
