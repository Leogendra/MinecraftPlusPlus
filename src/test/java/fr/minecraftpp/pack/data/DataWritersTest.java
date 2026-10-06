package fr.minecraftpp.pack.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.GoldenFixture;
import fr.minecraftpp.core.TestCatalogs;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.pack.GeneratedPackContents;
import fr.minecraftpp.pack.GeneratedPackFixture;
import fr.minecraftpp.pack.asset.FlatItem;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

/**
 * Seed 42: kal is a familiar material set whose ore needs a stone tool and drops one kal; dium, the copper set, is the other set mined with a stone tool. Its ore generates 20 veins of 9 blocks per chunk, below the 1.12 height 64.
 */
class DataWritersTest
{
	private static final Pattern MOD_IDENTIFIER = Pattern.compile("\"" + ContentIds.NAMESPACE + ":([a-z0-9_/]+)\"");

	@Test
	void filesMatchTheReferences()
	{
		GeneratedPackContents seed42 = GeneratedPackFixture.write(42);
		List<String> locations = List.of("minecraftpp:recipe/kal_pickaxe.json", "minecraftpp:recipe/kal_from_blasting_deepslate_kal_ore.json", "minecraftpp:loot_table/blocks/kal_ore.json", "minecraftpp:loot_table/blocks/kal_block.json", "minecraft:tags/block/needs_stone_tool.json", "minecraftpp:worldgen/configured_feature/kal_ore.json", "minecraftpp:worldgen/placed_feature/kal_ore.json");

		for (String location : locations)
		{
			Identifier identifier = Identifier.parse(location);

			assertEquals(GeneratedPackFixture.reference(PackType.SERVER_DATA, identifier), GeneratedPackFixture.file(seed42, PackType.SERVER_DATA, identifier), location);
		}
	}

	/**
	 * Every block and item the recipes, loot tables and tags name in the mod namespace is registered by the mod; the loot sequences name a loot table, not an item.
	 */
	@Test
	void everyNamedContentExists()
	{
		for (long seed : GoldenFixture.SEEDS)
		{
			Set<String> contentIds = contentIds(seed);
			GeneratedPackContents contents = GeneratedPackFixture.write(seed);

			for (String namespace : List.of("minecraft", ContentIds.NAMESPACE))
			{
				for (Identifier location : contents.resources(PackType.SERVER_DATA, namespace, "").keySet())
				{
					String json = GeneratedPackFixture.file(contents, PackType.SERVER_DATA, location).toString().replaceAll("\"random_sequence\":\"[^\"]*\"", "");
					Matcher matcher = MOD_IDENTIFIER.matcher(json);

					while (matcher.find())
					{
						assertTrue(contentIds.contains(matcher.group(1)), location + " names the unknown content " + matcher.group());
					}
				}
			}
		}
	}

	private static Set<String> contentIds(long seed)
	{
		Set<String> ids = new HashSet<>();

		for (OreSetDefinition set : TestCatalogs.generate(seed).sets())
		{
			ids.addAll(ContentIds.blocks(set));
			FlatItem.of(set).forEach(item -> ids.add(item.itemId()));
		}

		return ids;
	}
}
