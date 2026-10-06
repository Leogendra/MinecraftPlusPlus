package fr.minecraftpp.content;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import net.fabricmc.fabric.api.registry.FuelValueEvents;

/**
 * Registers the burn time of the fuel items and blocks in the furnaces.
 */
public final class FuelRegistration
{
	private FuelRegistration()
	{
	}

	public static void register(OreCatalog catalog, RegisteredContent content)
	{
		Map<String, Integer> burnTimes = burnTimes(catalog);

		FuelValueEvents.BUILD.register((builder, context) -> burnTimes.forEach((path, ticks) -> builder.add(content.item(path), ticks)));
	}

	/**
	 * The burn time in ticks of each fuel, by identifier path. A fuel set burns ten times longer as a block.
	 */
	public static Map<String, Integer> burnTimes(OreCatalog catalog)
	{
		Map<String, Integer> burnTimes = new LinkedHashMap<>();

		for (OreSetDefinition set : catalog.sets())
		{
			if (set.item().fuelTicks() > 0)
			{
				burnTimes.put(ContentIds.item(set), set.item().fuelTicks());
			}

			if (set.block().fuelTicks() > 0)
			{
				burnTimes.put(ContentIds.storageBlock(set), set.block().fuelTicks());
			}
		}

		return burnTimes;
	}
}
