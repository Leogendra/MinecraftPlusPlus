package fr.minecraftpp.pack.vanilla;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import fr.minecraftpp.core.set.TagIds;
import fr.minecraftpp.core.variant.VariantCatalog;

/**
 * Makes the ingredients of a vanilla JSON file accept the generated variants, as 1.12 accepted a variant wherever the vanilla item was:
 * <ul>
 * <li>an item with variants becomes the tag of its variants;</li>
 * <li>a list of items receives the variants of its items, since a list cannot hold a tag;</li>
 * <li>a vanilla tag holding an item with variants becomes a tag of the vanilla tag and these variants; the vanilla tag itself is left unchanged.</li>
 * </ul>
 * The {@code result} and the {@code pattern} of a recipe are never rewritten. The elements of an {@code ingredients} list are each an ingredient.
 */
public final class VariantIngredients
{
	private static final String RESULT_KEY = "result";
	private static final String PATTERN_KEY = "pattern";
	private static final String INGREDIENTS_KEY = "ingredients";

	private final VariantCatalog variants;
	private final Map<String, List<String>> vanillaTagMembers;
	private final Set<String> rewrittenTags = new TreeSet<>();

	public VariantIngredients(VariantCatalog variants, Map<String, List<String>> vanillaTagMembers)
	{
		this.variants = variants;
		this.vanillaTagMembers = vanillaTagMembers;
	}

	/**
	 * Rewrites the ingredients found in a recipe: any value except the result, the pattern and the {@code ingredients} list, whose elements are each an ingredient.
	 */
	public JsonElement rewrite(JsonElement value)
	{
		if (value.isJsonObject())
		{
			return this.rewriteObject(value.getAsJsonObject());
		}
		else if (isIngredient(value))
		{
			return this.rewriteIngredient(value);
		}
		else if (value.isJsonArray())
		{
			JsonArray rewritten = new JsonArray();
			value.getAsJsonArray().forEach(element -> rewritten.add(this.rewrite(element)));
			return rewritten;
		}
		else
		{
			return value;
		}
	}

	/**
	 * The vanilla tags replaced by a variant tag so far, without {@code #}: their variant tags must be written.
	 */
	public Set<String> rewrittenTags()
	{
		return this.rewrittenTags;
	}

	/**
	 * The variants of the items of a vanilla tag, for its variant tag.
	 */
	public List<String> variantsOfTag(String vanillaTagId)
	{
		return this.vanillaTagMembers.getOrDefault(vanillaTagId, List.of()).stream().flatMap(member -> this.variants.variantsOf(member).stream()).toList();
	}

	private JsonObject rewriteObject(JsonObject object)
	{
		JsonObject rewritten = new JsonObject();

		for (Map.Entry<String, JsonElement> entry : object.entrySet())
		{
			rewritten.add(entry.getKey(), this.rewriteEntry(entry.getKey(), entry.getValue()));
		}

		return rewritten;
	}

	private JsonElement rewriteEntry(String key, JsonElement value)
	{
		if (key.equals(RESULT_KEY) || key.equals(PATTERN_KEY))
		{
			return value;
		}
		else if (key.equals(INGREDIENTS_KEY) && value.isJsonArray())
		{
			JsonArray rewritten = new JsonArray();
			value.getAsJsonArray().forEach(ingredient -> rewritten.add(this.rewrite(ingredient)));
			return rewritten;
		}
		else
		{
			return this.rewrite(value);
		}
	}

	/**
	 * An ingredient is an item or a tag, or a list of items.
	 */
	private static boolean isIngredient(JsonElement value)
	{
		if (value.isJsonPrimitive())
		{
			return value.getAsJsonPrimitive().isString();
		}
		else if (value.isJsonArray())
		{
			return value.getAsJsonArray().asList().stream().allMatch(element -> element.isJsonPrimitive() && element.getAsJsonPrimitive().isString());
		}
		else
		{
			return false;
		}
	}

	private JsonElement rewriteIngredient(JsonElement ingredient)
	{
		if (ingredient.isJsonArray())
		{
			JsonArray items = new JsonArray();

			for (JsonElement item : ingredient.getAsJsonArray())
			{
				items.add(item);
				this.variants.variantsOf(item.getAsString()).forEach(items::add);
			}

			return items;
		}
		else
		{
			return new JsonPrimitive(this.rewriteIdentifier(ingredient.getAsString()));
		}
	}

	private String rewriteIdentifier(String identifier)
	{
		if (identifier.startsWith("#") && !this.variantsOfTag(identifier.substring(1)).isEmpty())
		{
			this.rewrittenTags.add(identifier.substring(1));
			return "#" + TagIds.variantsOfTag(identifier.substring(1));
		}
		else if (this.variants.hasVariants(identifier))
		{
			return "#" + TagIds.variantsOf(identifier);
		}
		else
		{
			return identifier;
		}
	}
}
