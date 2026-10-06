package fr.minecraftpp.pack.data;

import java.util.List;

import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreGeneration;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.world.OreHeightMapping;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;
import fr.minecraftpp.pack.data.OreFeatureJson.Placement;
import net.minecraft.resources.Identifier;

/**
 * Writes the world generation of each ore, as 1.12 placed it: a number of veins per chunk, of a number of blocks, at a uniform height up to a maximum, converted by {@link OreHeightMapping}. The vein becomes the deepslate ore where it crosses deepslate.
 */
public final class OreFeatureWriter implements GeneratedResourceWriter
{
	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		return catalog.sets().stream().flatMap(set -> List.of(configured(set), placed(set)).stream()).toList();
	}

	/**
	 * The identifier of both the configured and the placed feature of the ore of a set.
	 */
	public static Identifier featureId(OreSetDefinition set)
	{
		return Identifier.fromNamespaceAndPath(ContentIds.NAMESPACE, ContentIds.ore(set));
	}

	private static GeneratedFile configured(OreSetDefinition set)
	{
		OreFeatureJson.Target stone = new OreFeatureJson.Target(new OreFeatureJson.BlockStateJson(ContentIds.full(ContentIds.ore(set))), new OreFeatureJson.RuleTest("minecraft:tag_match", "minecraft:stone_ore_replaceables"));
		OreFeatureJson.Target deepslate = new OreFeatureJson.Target(new OreFeatureJson.BlockStateJson(ContentIds.full(ContentIds.deepslateOre(set))), new OreFeatureJson.RuleTest("minecraft:tag_match", "minecraft:deepslate_ore_replaceables"));
		OreFeatureJson.OreConfig config = new OreFeatureJson.OreConfig(0.0F, set.generation().veinDensity(), List.of(stone, deepslate));

		return GeneratedFile.data(featureId(set).withPath(path -> "worldgen/configured_feature/" + path + ".json"), new OreFeatureJson.Configured("minecraft:ore", config));
	}

	private static GeneratedFile placed(OreSetDefinition set)
	{
		OreGeneration generation = set.generation();
		List<Placement> placement = List.of(Placement.count(OreHeightMapping.veinsPerChunk(generation)), Placement.inSquare(), Placement.uniformHeight(OreHeightMapping.minInclusive(), OreHeightMapping.maxInclusive(generation)), Placement.biome());

		return GeneratedFile.data(featureId(set).withPath(path -> "worldgen/placed_feature/" + path + ".json"), new OreFeatureJson.Placed(featureId(set).toString(), placement));
	}
}
