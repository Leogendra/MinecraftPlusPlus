package fr.minecraftpp.pack.asset;

import java.util.List;
import java.util.Map;

import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;

/**
 * Writes the models of the items drawn from a single texture. The block items have no item model: they are drawn with their block model.
 */
public final class ItemModelWriter implements GeneratedResourceWriter
{
	private static final String FLAT_PARENT = "minecraft:item/generated";
	private static final String HANDHELD_PARENT = "minecraft:item/handheld";

	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		return catalog.sets().stream().flatMap(set -> FlatItem.of(set).stream()).map(ItemModelWriter::model).toList();
	}

	private static GeneratedFile model(FlatItem item)
	{
		String parent = item.handheld() ? HANDHELD_PARENT : FLAT_PARENT;

		return GeneratedFile.asset("models/item/" + item.itemId() + ".json", new ModelJson(parent, Map.of("layer0", item.texture())));
	}
}
