package fr.minecraftpp.core.set.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.TestCatalogs;
import fr.minecraftpp.core.naming.NameGenerator;
import fr.minecraftpp.core.set.OreSetDefinition;

class SetNameUniquenessTest
{
	@Test
	void setsGetDistinctNamesEvenWhenTheGeneratorRepeatsOne()
	{
		long seed = firstSeedWhoseFirstSevenNamesRepeat();

		List<String> names = TestCatalogs.generate(seed).sets().stream().map(OreSetDefinition::name).toList();

		assertEquals(7, new HashSet<>(names).size(), "seed " + seed + ": " + names);
	}

	private static long firstSeedWhoseFirstSevenNamesRepeat()
	{
		for (long seed = 0; seed < 10_000; seed++)
		{
			NameGenerator names = TestCatalogs.nameGenerator(seed);
			Set<String> seen = new HashSet<>();

			for (int i = 0; i < 7; i++)
			{
				if (!seen.add(names.nextName()))
				{
					return seed;
				}
			}
		}

		throw new AssertionError("No seed repeats a name, the test needs another range");
	}
}
