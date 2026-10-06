package fr.minecraftpp.pack.data;

import java.util.List;

import fr.minecraftpp.content.material.MaterialFactory;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;

/**
 * Writes, for each set with tools and armor, the tag their material names as repair material: the main item of the set.
 */
public final class RepairMaterialTagWriter implements GeneratedResourceWriter
{
	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		return catalog.sets().stream().filter(set -> set.material().isPresent()).map(RepairMaterialTagWriter::repairMaterials).toList();
	}

	private static GeneratedFile repairMaterials(OreSetDefinition set)
	{
		return new TagJson(List.of(ContentIds.full(ContentIds.item(set)))).toFile(MaterialFactory.repairMaterials(set));
	}
}
