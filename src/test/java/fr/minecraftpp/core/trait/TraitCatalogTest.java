package fr.minecraftpp.core.trait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

class TraitCatalogTest
{
	@Test
	void defaultsAreThe112Chances()
	{
		TraitCatalog catalog = TraitCatalog.defaults();

		assertEquals(20, catalog.oneIn(Trait.SHINY));
		assertEquals(7, catalog.oneIn(Trait.EDIBLE));
		assertEquals(50, catalog.oneIn(Trait.ABSORBS_WATER));
		assertEquals(33, catalog.oneIn(Trait.FLAMMABLE));
	}

	@Test
	void requiresEveryTrait()
	{
		Map<Trait, Integer> chances = new EnumMap<>(TraitCatalog.defaults().oneIn());
		chances.remove(Trait.FALLS);

		assertThrows(IllegalArgumentException.class, () -> new TraitCatalog(chances));
	}

	@Test
	void rejectsChancesBelowOne()
	{
		Map<Trait, Integer> chances = new EnumMap<>(TraitCatalog.defaults().oneIn());
		chances.put(Trait.SHINY, 0);

		assertThrows(IllegalArgumentException.class, () -> new TraitCatalog(chances));
	}

	@Test
	void drawConsumesTheSameRandomValueAsThe112Code()
	{
		Random catalogRandom = new Random(5);
		Random legacyRandom = new Random(5);
		TraitCatalog catalog = TraitCatalog.defaults();

		for (int i = 0; i < 100; i++)
		{
			assertEquals(legacyRandom.nextInt(15) == 0, catalog.draw(Trait.FALLS, catalogRandom));
		}

		assertEquals(legacyRandom.nextLong(), catalogRandom.nextLong());
	}
}
