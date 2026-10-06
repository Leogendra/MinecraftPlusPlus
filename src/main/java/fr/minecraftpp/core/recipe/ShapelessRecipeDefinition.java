package fr.minecraftpp.core.recipe;

import java.util.List;

/**
 * A crafting recipe whose ingredients can be placed anywhere in the grid.
 */
public record ShapelessRecipeDefinition(String id, List<Ingredient> ingredients, String result, int count, RecipeCategory category) implements RecipeDefinition
{
	public ShapelessRecipeDefinition
	{
		ingredients = List.copyOf(ingredients);

		if (ingredients.isEmpty() || ingredients.size() > 9)
		{
			throw new IllegalArgumentException("Recipe " + id + " needs between 1 and 9 ingredients");
		}

		if (count < 1)
		{
			throw new IllegalArgumentException("Recipe " + id + " must produce at least one item");
		}
	}
}
