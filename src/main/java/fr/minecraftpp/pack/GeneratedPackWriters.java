package fr.minecraftpp.pack;

import java.util.List;

import fr.minecraftpp.pack.asset.BlockModelWriter;
import fr.minecraftpp.pack.asset.BlockStateWriter;
import fr.minecraftpp.pack.asset.ItemDefinitionWriter;
import fr.minecraftpp.pack.asset.ItemModelWriter;
import fr.minecraftpp.pack.data.LootTableWriter;
import fr.minecraftpp.pack.data.OreFeatureWriter;
import fr.minecraftpp.pack.data.RecipeWriter;
import fr.minecraftpp.pack.data.RepairMaterialTagWriter;
import fr.minecraftpp.pack.data.TagWriter;
import fr.minecraftpp.pack.vanilla.VanillaData;
import fr.minecraftpp.pack.vanilla.VanillaRecipeRewriter;
import fr.minecraftpp.pack.vanilla.VariantTagWriter;

/**
 * The writers of every file of the generated pack: client resources first, then server data, then the rewritten vanilla data.
 */
public final class GeneratedPackWriters
{
	private GeneratedPackWriters()
	{
	}

	/**
	 * @param vanillaData the vanilla files that the generated pack rewrites to accept the variants
	 */
	public static List<GeneratedResourceWriter> all(VanillaData vanillaData)
	{
		return List.of(new BlockStateWriter(), new BlockModelWriter(), new ItemModelWriter(), new ItemDefinitionWriter(), new RecipeWriter(), new LootTableWriter(), new TagWriter(), new RepairMaterialTagWriter(), new OreFeatureWriter(), new VariantTagWriter(), new VanillaRecipeRewriter(vanillaData));
	}
}
