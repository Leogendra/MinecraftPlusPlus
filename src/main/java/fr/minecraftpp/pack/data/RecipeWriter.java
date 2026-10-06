package fr.minecraftpp.pack.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;

import fr.minecraftpp.core.recipe.RecipeDefinition;
import fr.minecraftpp.core.recipe.RecipePlanner;
import fr.minecraftpp.core.recipe.ShapedRecipeDefinition;
import fr.minecraftpp.core.recipe.ShapelessRecipeDefinition;
import fr.minecraftpp.core.recipe.SmeltingRecipeDefinition;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.pack.GeneratedFile;
import fr.minecraftpp.pack.GeneratedResourceWriter;
import net.minecraft.resources.Identifier;

/**
 * Writes the recipes planned by the core as vanilla recipe files.
 */
public final class RecipeWriter implements GeneratedResourceWriter
{
	@Override
	public List<GeneratedFile> write(OreCatalog catalog)
	{
		return RecipePlanner.plan(catalog).stream().map(RecipeWriter::file).toList();
	}

	private static GeneratedFile file(RecipeDefinition recipe)
	{
		Object json = switch (recipe)
		{
			case ShapedRecipeDefinition shaped -> shaped(shaped);
			case ShapelessRecipeDefinition shapeless -> shapeless(shapeless);
			case SmeltingRecipeDefinition smelting -> cooking(smelting);
		};

		return GeneratedFile.data(Identifier.fromNamespaceAndPath(ContentIds.NAMESPACE, "recipe/" + recipe.id() + ".json"), json);
	}

	private static RecipeJson.Shaped shaped(ShapedRecipeDefinition recipe)
	{
		Map<String, JsonElement> key = new LinkedHashMap<>();
		recipe.key().forEach((symbol, ingredient) -> key.put(String.valueOf(symbol), RecipeJson.ingredient(ingredient)));

		return new RecipeJson.Shaped("minecraft:crafting_shaped", RecipeJson.category(recipe.category()), key, recipe.pattern(), new RecipeJson.Result(recipe.result(), recipe.count()));
	}

	private static RecipeJson.Shapeless shapeless(ShapelessRecipeDefinition recipe)
	{
		List<JsonElement> ingredients = recipe.ingredients().stream().map(RecipeJson::ingredient).toList();

		return new RecipeJson.Shapeless("minecraft:crafting_shapeless", RecipeJson.category(recipe.category()), ingredients, new RecipeJson.Result(recipe.result(), recipe.count()));
	}

	private static RecipeJson.Cooking cooking(SmeltingRecipeDefinition recipe)
	{
		String type = switch (recipe.method())
		{
			case SMELTING -> "minecraft:smelting";
			case BLASTING -> "minecraft:blasting";
		};

		return new RecipeJson.Cooking(type, RecipeJson.category(recipe.category()), recipe.method().getCookingTime(), recipe.experience(), RecipeJson.ingredient(recipe.ingredient()), new RecipeJson.Result(recipe.result(), 1));
	}
}
