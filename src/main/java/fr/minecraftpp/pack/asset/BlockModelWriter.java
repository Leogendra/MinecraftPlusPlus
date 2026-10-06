package fr.minecraftpp.pack.asset;

import java.util.List;
import java.util.Map;

import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;

/**
 * Writes the block models, as in 1.12: the storage block is a tinted cube, the ore a tinted overlay drawn over stone or deepslate. The two parent models are static files of the mod jar.
 */
public final class BlockModelWriter implements GeneratedResourceWriter
{
	public static final String TINTED_CUBE = ContentIds.full("block/tinted_cube");
	public static final String TINTED_OVERLAY_CUBE = ContentIds.full("block/tinted_overlay_cube");

	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		return catalog.sets().stream().flatMap(set -> models(set).stream()).toList();
	}

	private static List<GeneratedFile> models(OreSetDefinition set)
	{
		ModelJson ore = new ModelJson(TINTED_OVERLAY_CUBE, Map.of("background", TextureIds.STONE, "overlay", TextureIds.oreOverlay(set)));
		ModelJson deepslateOre = new ModelJson(TINTED_OVERLAY_CUBE, Map.of("background", TextureIds.DEEPSLATE, "overlay", TextureIds.oreOverlay(set)));
		ModelJson storageBlock = new ModelJson(TINTED_CUBE, Map.of("all", TextureIds.storageBlock(set)));

		return List.of(model(ContentIds.ore(set), ore), model(ContentIds.deepslateOre(set), deepslateOre), model(ContentIds.storageBlock(set), storageBlock));
	}

	private static GeneratedFile model(String blockId, ModelJson model)
	{
		return GeneratedFile.asset("models/block/" + blockId + ".json", model);
	}
}
