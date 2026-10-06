package fr.minecraftpp.pack.data;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import fr.minecraftpp.core.recipe.Ingredient;
import fr.minecraftpp.core.recipe.RecipeCategory;

/**
 * The shapes of the vanilla recipe files. An ingredient is written as the game reads it: an item, a list of items, or a tag starting with {@code #}.
 */
public final class RecipeJson
{
	private RecipeJson()
	{
	}

	public static JsonElement ingredient(Ingredient ingredient)
	{
		if (ingredient.isTag())
		{
			return new JsonPrimitive("#" + ingredient.ids().getFirst());
		}
		else if (ingredient.ids().size() == 1)
		{
			return new JsonPrimitive(ingredient.ids().getFirst());
		}
		else
		{
			JsonArray items = new JsonArray();
			ingredient.ids().forEach(items::add);
			return items;
		}
	}

	public static String category(RecipeCategory category)
	{
		return category.name().toLowerCase(Locale.ROOT);
	}

	public record Shaped(String type, String category, Map<String, JsonElement> key, List<String> pattern, Result result)
	{
	}

	public record Shapeless(String type, String category, List<JsonElement> ingredients, Result result)
	{
	}

	public record Cooking(String type, String category, int cookingtime, float experience, JsonElement ingredient, Result result)
	{
	}

	public record Result(String id, int count)
	{
	}
}
