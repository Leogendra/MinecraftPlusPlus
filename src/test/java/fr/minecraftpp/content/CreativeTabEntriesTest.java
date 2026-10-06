package fr.minecraftpp.content;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.content.CreativeTabEntries.Tab;
import fr.minecraftpp.core.TestCatalogs;

/**
 * Seed 42: cychotinte (metal), kal and rize (material), then four simple sets.
 */
class CreativeTabEntriesTest
{
	private static final Map<Tab, List<String>> ENTRIES = CreativeTabEntries.entries(TestCatalogs.generate(42));

	@Test
	void listsTheBlocksSetBySet()
	{
		assertEquals(List.of("cychotinte_ore", "deepslate_cychotinte_ore", "kal_ore", "deepslate_kal_ore"), ENTRIES.get(Tab.NATURAL_BLOCKS).subList(0, 4));
		assertEquals(7, ENTRIES.get(Tab.BUILDING_BLOCKS).size());
	}

	@Test
	void listsTheIngotsWithTheirNuggets()
	{
		assertEquals(List.of("cychotinte_ingot", "cychotinte_nugget", "kal", "rize"), ENTRIES.get(Tab.INGREDIENTS).subList(0, 4));
	}

	@Test
	void listsTheEquipmentOfMaterialSetsOnly()
	{
		assertEquals(List.of("cychotinte_shovel", "cychotinte_pickaxe", "cychotinte_axe", "cychotinte_hoe"), ENTRIES.get(Tab.TOOLS_AND_UTILITIES).subList(0, 4));
		assertEquals(3 * 4, ENTRIES.get(Tab.TOOLS_AND_UTILITIES).size());
		assertTrue(ENTRIES.get(Tab.COMBAT).contains("rize_chestplate"));
		assertFalse(ENTRIES.get(Tab.COMBAT).stream().anyMatch(path -> path.startsWith("derepess")));
	}
}
