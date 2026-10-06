package fr.minecraftpp.core.set.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.GoldenDetails;
import fr.minecraftpp.core.GoldenFixture;
import fr.minecraftpp.core.GoldenFixture.GoldenSet;
import fr.minecraftpp.core.TestCatalogs;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.material.ArmorPiece;
import fr.minecraftpp.core.set.material.ToolType;
import fr.minecraftpp.core.text.DisplayNameFormatter;
import fr.minecraftpp.core.text.SetInfoFormatter;

/**
 * Main regression test of the port: for each golden seed, the generated catalog must match the 1.12 capture value for value.
 */
class OreCatalogGoldenTest
{
	@Test
	void mppInfoTextMatchesThe112Text()
	{
		for (long seed : GoldenFixture.SEEDS)
		{
			OreCatalog catalog = TestCatalogs.generate112(seed);

			assertEquals(String.join("\n", GoldenFixture.load(seed).infoLines()), SetInfoFormatter.format(catalog), "seed " + seed);
		}
	}

	@Test
	void everyGeneratedValueMatchesThe112Value()
	{
		for (long seed : GoldenFixture.SEEDS)
		{
			List<OreSetDefinition> sets = TestCatalogs.generate112(seed).sets();
			List<GoldenSet> goldenSets = GoldenFixture.load(seed).sets();

			assertEquals(goldenSets.size(), sets.size(), "seed " + seed);
			for (int index = 0; index < sets.size(); index++)
			{
				assertEquals(goldenSets.get(index).type(), GoldenDetails.className(sets.get(index).type()), "type, seed " + seed + ", set " + index);
				assertEquals(goldenSets.get(index).values(), GoldenDetails.of(sets.get(index)), "seed " + seed + ", set " + index);
			}
		}
	}

	@Test
	void displayNamesMatchThe112Translations()
	{
		for (long seed : GoldenFixture.SEEDS)
		{
			OreCatalog catalog = TestCatalogs.generate112(seed);
			Map<String, String> translations = GoldenFixture.load(seed).translations();

			List<String> expectedNames = translations.entrySet().stream().filter(entry -> !entry.getKey().startsWith("death.")).map(Map.Entry::getValue).sorted().toList();
			List<String> expectedDeathMessages = translations.entrySet().stream().filter(entry -> entry.getKey().startsWith("death.")).map(Map.Entry::getValue).sorted().toList();

			assertEquals(expectedNames, displayNames(catalog), "seed " + seed);
			assertEquals(expectedDeathMessages, catalog.sets().stream().filter(set -> set.block().walkDamage() > 0).map(DisplayNameFormatter::walkDamageDeathMessage).sorted().toList(), "seed " + seed);
		}
	}

	private static List<String> displayNames(OreCatalog catalog)
	{
		List<String> names = new ArrayList<>();

		for (OreSetDefinition set : catalog.sets())
		{
			names.add(DisplayNameFormatter.itemName(set));
			names.add(DisplayNameFormatter.storageBlockName(set));
			names.add(DisplayNameFormatter.oreName(set));

			if (set.type() == SetType.METAL)
			{
				names.add(DisplayNameFormatter.nuggetName(set));
			}

			if (set.type().hasMaterial())
			{
				Arrays.stream(ToolType.values()).map(toolType -> DisplayNameFormatter.toolName(set, toolType)).forEach(names::add);
				Arrays.stream(ArmorPiece.values()).map(piece -> DisplayNameFormatter.armorName(set, piece)).forEach(names::add);
			}
		}

		return names.stream().sorted().toList();
	}
}
