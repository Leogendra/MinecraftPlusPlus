package fr.minecraftpp.core.recipe;

/**
 * A recipe of the generated content, independent of the game: the pack writers turn it into a recipe file.
 */
public sealed interface RecipeDefinition permits ShapedRecipeDefinition, ShapelessRecipeDefinition, SmeltingRecipeDefinition
{
	/**
	 * Identifier of the recipe in the mod namespace, without the namespace.
	 */
	String id();

	/**
	 * Complete identifier of the produced item.
	 */
	String result();

	RecipeCategory category();
}
