package fr.minecraftpp.pack.asset;

import java.util.ArrayList;
import java.util.List;

import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;

/**
 * Writes the item definition of every generated item, tinted with the color of its set as in 1.12. A block item points to its block model, the other items to their item model.
 */
public final class ItemDefinitionWriter implements GeneratedResourceWriter
{
	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		return catalog.sets().stream().flatMap(set -> definitions(set).stream()).toList();
	}

	private static List<GeneratedFile> definitions(OreSetDefinition set)
	{
		int color = set.item().color().asInt();
		List<GeneratedFile> definitions = new ArrayList<>();

		for (String blockId : ContentIds.blocks(set))
		{
			definitions.add(definition(blockId, ContentIds.full("block/" + blockId), color));
		}

		for (FlatItem item : FlatItem.of(set))
		{
			definitions.add(definition(item.itemId(), ContentIds.full("item/" + item.itemId()), color));
		}

		return definitions;
	}

	private static GeneratedFile definition(String itemId, String model, int color)
	{
		return GeneratedFile.asset("items/" + itemId + ".json", ItemDefinitionJson.tinted(model, color));
	}
}
