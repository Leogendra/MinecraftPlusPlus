package fr.minecraftpp.pack.vanilla;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonElement;

import net.minecraft.resources.Identifier;

/**
 * The vanilla data files the generated pack rewrites: recipes, loot tables, villager trades and tags.
 */
public interface VanillaData
{
	String ITEM_TAGS_DIRECTORY = "tags/item";

	/**
	 * The JSON files of the {@code minecraft} namespace below a directory of the vanilla data, such as {@code recipe}, by location ({@code minecraft:recipe/bucket.json}).
	 */
	Map<Identifier, JsonElement> files(String directory);

	/**
	 * The direct members of each vanilla item tag, such as {@code minecraft:coals} with {@code minecraft:coal} and {@code minecraft:charcoal}. Nested tags are kept as {@code #} entries, not expanded.
	 */
	default Map<String, List<String>> itemTagMembers()
	{
		Map<String, List<String>> members = new TreeMap<>();

		this.files(ITEM_TAGS_DIRECTORY).forEach((location, json) ->
		{
			String path = location.getPath().substring(ITEM_TAGS_DIRECTORY.length() + 1, location.getPath().length() - ".json".length());
			List<String> values = json.getAsJsonObject().getAsJsonArray("values").asList().stream().filter(JsonElement::isJsonPrimitive).map(JsonElement::getAsString).toList();

			members.put(location.getNamespace() + ":" + path, values);
		});

		return members;
	}
}
