package fr.minecraftpp.core.ore;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;

class OreRarityTest
{
	private static final Set<Integer> MAX_HEIGHTS = Set.of(128, 64, 32, 16);

	@Test
	void veinsStayWithinTheirBounds()
	{
		List<List<OreProperties>> propertyLists = List.of(List.of(), List.of(OreProperties.COAL, OreProperties.FUEL, OreProperties.BEACON), List.of(OreProperties.DIAMOND, OreProperties.REDSTONE, OreProperties.IRON, OreProperties.GOLD));

		for (long seed = 0; seed < 200; seed++)
		{
			for (List<OreProperties> properties : propertyLists)
			{
				OreRarity rarity = OreRarity.getRarityFrom(properties, new Random(seed));

				assertTrue(rarity.getVeinAmount() >= 1 && rarity.getVeinAmount() <= 20, "vein amount " + rarity.getVeinAmount());
				assertTrue(rarity.getVeinDensity() >= 1 && rarity.getVeinDensity() <= 20, "vein density " + rarity.getVeinDensity());
				assertTrue(MAX_HEIGHTS.contains(rarity.getMaxHeight()), "max height " + rarity.getMaxHeight());
			}
		}
	}

	@Test
	void theRarestRoleSetsTheMaximumHeight()
	{
		assertTrue(OreRarity.getRarityFrom(List.of(OreProperties.COAL), new Random(0)).getMaxHeight() == 128);
		assertTrue(OreRarity.getRarityFrom(List.of(OreProperties.COAL, OreProperties.IRON), new Random(0)).getMaxHeight() == 64);
		assertTrue(OreRarity.getRarityFrom(List.of(OreProperties.GOLD), new Random(0)).getMaxHeight() == 32);
		assertTrue(OreRarity.getRarityFrom(List.of(OreProperties.DIAMOND, OreProperties.COAL), new Random(0)).getMaxHeight() == 16);
	}
}
