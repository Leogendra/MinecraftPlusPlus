package fr.minecraftpp.pack.vanilla;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.variant.VariantCatalog;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;

/**
 * Overrides the vanilla loot tables that drop an item with generated variants: the item is replaced by one of its variants, drawn with the same chance each, as the 1.12 loot table patch did. The entry keeps its weight, conditions and functions, so the stack size is kept; 1.12 reset it to one.
 *
 * The block tables stay vanilla: 1.12 blocks did not use loot tables, and a placed vanilla iron block must still drop itself.
 */
public final class VanillaLootTableRewriter implements GeneratedResourceWriter
{
	private static final String LOOT_TABLE_DIRECTORY = "loot_table";
	private static final String BLOCK_TABLES = LOOT_TABLE_DIRECTORY + "/blocks/";
	private static final String ITEM_ENTRY = "minecraft:item";
	private static final String NESTED_TABLE_ENTRY = "minecraft:loot_table";

	private final VanillaData vanillaData;

	public VanillaLootTableRewriter(VanillaData vanillaData)
	{
		this.vanillaData = vanillaData;
	}

	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		VariantCatalog variants = VariantCatalog.of(catalog);
		List<GeneratedFile> files = new ArrayList<>();

		this.vanillaData.files(LOOT_TABLE_DIRECTORY).forEach((location, table) ->
		{
			if (!location.getPath().startsWith(BLOCK_TABLES))
			{
				JsonElement rewritten = rewrite(table, variants);

				if (!rewritten.equals(table))
				{
					files.add(GeneratedFile.data(location, rewritten));
				}
			}
		});

		return files;
	}

	private static JsonElement rewrite(JsonElement value, VariantCatalog variants)
	{
		if (value.isJsonObject() && isItemEntryWithVariants(value.getAsJsonObject(), variants))
		{
			return variantDraw(value.getAsJsonObject(), variants);
		}
		else if (value.isJsonObject())
		{
			JsonObject rewritten = new JsonObject();
			value.getAsJsonObject().entrySet().forEach(entry -> rewritten.add(entry.getKey(), rewrite(entry.getValue(), variants)));
			return rewritten;
		}
		else if (value.isJsonArray())
		{
			JsonArray rewritten = new JsonArray();
			value.getAsJsonArray().forEach(element -> rewritten.add(rewrite(element, variants)));
			return rewritten;
		}
		else
		{
			return value;
		}
	}

	private static boolean isItemEntryWithVariants(JsonObject object, VariantCatalog variants)
	{
		return object.has("type") && object.get("type").getAsString().equals(ITEM_ENTRY) && object.has("name") && variants.hasVariants(object.get("name").getAsString());
	}

	/**
	 * The item entry becomes a nested table of one roll among the variants, all of weight one.
	 */
	private static JsonObject variantDraw(JsonObject itemEntry, VariantCatalog variants)
	{
		JsonArray variantEntries = new JsonArray();

		for (String variant : variants.variantsOf(itemEntry.get("name").getAsString()))
		{
			JsonObject variantEntry = new JsonObject();
			variantEntry.addProperty("type", ITEM_ENTRY);
			variantEntry.addProperty("name", variant);
			variantEntries.add(variantEntry);
		}

		JsonObject pool = new JsonObject();
		pool.addProperty("rolls", 1.0F);
		pool.add("entries", variantEntries);

		JsonArray pools = new JsonArray();
		pools.add(pool);

		JsonObject table = new JsonObject();
		table.add("pools", pools);

		JsonObject nestedEntry = new JsonObject();
		nestedEntry.addProperty("type", NESTED_TABLE_ENTRY);
		nestedEntry.add("value", table);

		for (Map.Entry<String, JsonElement> field : itemEntry.entrySet())
		{
			if (!field.getKey().equals("type") && !field.getKey().equals("name"))
			{
				nestedEntry.add(field.getKey(), field.getValue());
			}
		}

		return nestedEntry;
	}
}
