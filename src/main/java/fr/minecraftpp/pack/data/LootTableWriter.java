package fr.minecraftpp.pack.data;

import java.util.ArrayList;
import java.util.List;

import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreDrop;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;
import fr.minecraftpp.pack.data.LootTableJson.Condition;
import fr.minecraftpp.pack.data.LootTableJson.Entry;
import fr.minecraftpp.pack.data.LootTableJson.Function;
import fr.minecraftpp.pack.data.LootTableJson.Pool;
import net.minecraft.resources.Identifier;

/**
 * Writes the loot table of each block. The storage block and the metal ores drop themselves; a gem ore drops between the minimum and maximum items of its set, with the vanilla fortune bonus, or itself with silk touch. Mining with a too weak tool drops nothing: the blocks require the correct tool.
 */
public final class LootTableWriter implements GeneratedResourceWriter
{
	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		List<GeneratedFile> files = new ArrayList<>();

		for (OreSetDefinition set : catalog.sets())
		{
			files.add(file(ContentIds.ore(set), orePool(set, ContentIds.ore(set))));
			files.add(file(ContentIds.deepslateOre(set), orePool(set, ContentIds.deepslateOre(set))));
			files.add(file(ContentIds.storageBlock(set), selfDropPool(ContentIds.storageBlock(set))));
		}

		return files;
	}

	private static Pool orePool(OreSetDefinition set, String oreId)
	{
		return switch (set.ore().drop())
		{
			case OreDrop.Itself itself -> selfDropPool(oreId);
			case OreDrop.Items items -> itemsPool(set, oreId, items);
		};
	}

	private static Pool selfDropPool(String blockId)
	{
		return Pool.single(List.of(Condition.survivesExplosion()), Entry.item(ContentIds.full(blockId)));
	}

	private static Pool itemsPool(OreSetDefinition set, String oreId, OreDrop.Items items)
	{
		Entry silkTouchDrop = Entry.item(ContentIds.full(oreId), List.of(Condition.silkTouch()), null);
		Entry itemsDrop = Entry.item(ContentIds.full(ContentIds.item(set)), null, List.of(Function.setCount(items.minimum(), items.maximum()), Function.fortuneOreBonus(), Function.explosionDecay()));

		return Pool.single(null, Entry.alternatives(List.of(silkTouchDrop, itemsDrop)));
	}

	private static GeneratedFile file(String blockId, Pool pool)
	{
		String path = "blocks/" + blockId;

		return GeneratedFile.data(Identifier.fromNamespaceAndPath(ContentIds.NAMESPACE, "loot_table/" + path + ".json"), LootTableJson.block(ContentIds.full(path), pool));
	}
}
