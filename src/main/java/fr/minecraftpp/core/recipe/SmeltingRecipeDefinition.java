package fr.minecraftpp.core.recipe;

import java.util.Objects;

/**
 * A furnace or blast furnace recipe.
 */
public record SmeltingRecipeDefinition(String id, CookingMethod method, Ingredient ingredient, String result, float experience, RecipeCategory category) implements RecipeDefinition
{
	public SmeltingRecipeDefinition
	{
		Objects.requireNonNull(method, "method");
		Objects.requireNonNull(ingredient, "ingredient");

		if (experience < 0)
		{
			throw new IllegalArgumentException("Recipe " + id + " cannot give negative experience");
		}
	}

	/**
	 * Where the recipe cooks, and how long it takes in ticks, as in vanilla.
	 */
	public enum CookingMethod
	{
		SMELTING(200), BLASTING(100);

		private final int cookingTime;

		CookingMethod(int cookingTime)
		{
			this.cookingTime = cookingTime;
		}

		public int getCookingTime()
		{
			return this.cookingTime;
		}
	}
}
