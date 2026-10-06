package fr.minecraftpp.pack.vanilla;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.VanillaRole;
import fr.minecraftpp.core.variant.VariantCatalog;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;

/**
 * Overrides the vanilla villager trades, as 1.12 did: the villagers pay and are paid with the main item of the currency set instead of emeralds, and want or give the generated variant of an item instead of the vanilla item.
 *
 * A trade names a single item, never a tag. Each role goes to one set, so a vanilla item has a single variant.
 */
public final class VillagerTradeRewriter implements GeneratedResourceWriter
{
	private static final String TRADE_DIRECTORY = "villager_trade";
	private static final String EMERALD = "minecraft:emerald";
	private static final List<String> ITEM_FIELDS = List.of("wants", "additional_wants", "gives");

	private final VanillaData vanillaData;

	public VillagerTradeRewriter(VanillaData vanillaData)
	{
		this.vanillaData = vanillaData;
	}

	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		String currency = currency(catalog);
		VariantCatalog variants = VariantCatalog.of(catalog);
		List<GeneratedFile> files = new ArrayList<>();

		this.vanillaData.files(TRADE_DIRECTORY).forEach((location, trade) ->
		{
			JsonObject rewritten = rewrite(trade.getAsJsonObject(), currency, variants);

			if (!rewritten.equals(trade))
			{
				files.add(GeneratedFile.data(location, rewritten));
			}
		});

		return files;
	}

	/**
	 * The solver always gives the currency role to exactly one set.
	 */
	private static String currency(OreCatalog catalog)
	{
		List<OreSetDefinition> currencySets = catalog.setsWithRole(VanillaRole.CURRENCY);

		if (currencySets.size() != 1)
		{
			throw new IllegalStateException("Exactly one set should be the currency, not " + currencySets.size());
		}
		else
		{
			return ContentIds.full(ContentIds.item(currencySets.getFirst()));
		}
	}

	private static JsonObject rewrite(JsonObject trade, String currency, VariantCatalog variants)
	{
		JsonObject rewritten = trade.deepCopy();

		for (Map.Entry<String, JsonElement> field : rewritten.entrySet())
		{
			if (ITEM_FIELDS.contains(field.getKey()) && field.getValue().isJsonObject() && field.getValue().getAsJsonObject().has("id"))
			{
				JsonObject item = field.getValue().getAsJsonObject();
				item.addProperty("id", replacement(item.get("id").getAsString(), currency, variants));
			}
		}

		return rewritten;
	}

	private static String replacement(String itemId, String currency, VariantCatalog variants)
	{
		if (itemId.equals(EMERALD))
		{
			return currency;
		}
		else if (variants.hasVariants(itemId))
		{
			return variants.variantsOf(itemId).getFirst();
		}
		else
		{
			return itemId;
		}
	}
}
