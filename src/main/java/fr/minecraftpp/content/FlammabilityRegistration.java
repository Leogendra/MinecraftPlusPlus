package fr.minecraftpp.content;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.minecraftpp.core.ore.FlammabilityOf;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;

/**
 * Lets fire spread to and burn the flammable storage blocks, with the odds of the vanilla block they imitate.
 *
 * The blocks that burn forever, like netherrack, are not flammable: they go to the infiniburn tag of the generated pack.
 */
public final class FlammabilityRegistration
{
	private FlammabilityRegistration()
	{
	}

	public static void register(OreCatalog catalog, RegisteredContent content)
	{
		FlammableBlockRegistry registry = FlammableBlockRegistry.getDefaultInstance();

		flammableBlocks(catalog).forEach((path, flammability) -> registry.add(content.block(path), flammability.getEncouragement(), flammability.getFlammability()));
	}

	/**
	 * The flammable storage blocks, by identifier path.
	 */
	public static Map<String, FlammabilityOf> flammableBlocks(OreCatalog catalog)
	{
		Map<String, FlammabilityOf> flammableBlocks = new LinkedHashMap<>();

		for (OreSetDefinition set : catalog.sets())
		{
			if (set.block().flammability().isFlammable())
			{
				flammableBlocks.put(ContentIds.storageBlock(set), set.block().flammability());
			}
		}

		return flammableBlocks;
	}
}
