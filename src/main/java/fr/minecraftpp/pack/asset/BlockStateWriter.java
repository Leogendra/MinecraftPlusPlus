package fr.minecraftpp.pack.asset;

import java.util.List;

import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;

/**
 * Writes the block state definition of each generated block, which points to the block model of the same name.
 */
public final class BlockStateWriter implements GeneratedResourceWriter
{
	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		return catalog.sets().stream().flatMap(set -> ContentIds.blocks(set).stream()).map(BlockStateWriter::blockState).toList();
	}

	private static GeneratedFile blockState(String blockId)
	{
		return GeneratedFile.asset("blockstates/" + blockId + ".json", BlockStateJson.singleModel(ContentIds.full("block/" + blockId)));
	}
}
