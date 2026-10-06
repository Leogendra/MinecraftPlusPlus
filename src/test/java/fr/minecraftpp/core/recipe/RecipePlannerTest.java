package fr.minecraftpp.core.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.TestCatalogs;
import fr.minecraftpp.core.recipe.SmeltingRecipeDefinition.CookingMethod;

/**
 * Seed 42: cychotinte (metal, gold), kal (material, blue dye and iron), rize (material, diamond), derepess (coal), ovic (currency), dium (enchanting currency), citi (redstone).
 */
class RecipePlannerTest
{
	private static final Map<String, RecipeDefinition> RECIPES = RecipePlanner.plan(TestCatalogs.generate(42)).stream().collect(Collectors.toMap(RecipeDefinition::id, Function.identity()));

	@Test
	void recipeIdentifiersAreUnique()
	{
		List<RecipeDefinition> recipes = RecipePlanner.plan(TestCatalogs.generate(42));

		assertEquals(recipes.size(), RECIPES.size());
	}

	@Test
	void pickaxeUsesTheVanillaPattern()
	{
		ShapedRecipeDefinition pickaxe = (ShapedRecipeDefinition) RECIPES.get("kal_pickaxe");

		assertEquals(List.of("MMM", " S ", " S "), pickaxe.pattern());
		assertEquals(Ingredient.item("minecraftpp:kal"), pickaxe.key().get('M'));
		assertEquals(Ingredient.item("minecraft:stick"), pickaxe.key().get('S'));
		assertEquals("minecraftpp:kal_pickaxe", pickaxe.result());
	}

	@Test
	void smeltingExperienceGrowsWithTheRarity()
	{
		SmeltingRecipeDefinition familiar = (SmeltingRecipeDefinition) RECIPES.get("kal_from_smelting_kal_ore");
		SmeltingRecipeDefinition uncommon = (SmeltingRecipeDefinition) RECIPES.get("rize_from_smelting_deepslate_rize_ore");

		assertEquals(0.3F, familiar.experience());
		assertEquals(0.45F, uncommon.experience(), 1e-6);
		assertEquals(CookingMethod.SMELTING, familiar.method());
		assertTrue(RECIPES.containsKey("kal_from_blasting_kal_ore"));
	}

	@Test
	void metalSetsReceiveTheNuggetRecipes()
	{
		ShapelessRecipeDefinition nuggets = (ShapelessRecipeDefinition) RECIPES.get("cychotinte_nugget");

		assertEquals(9, nuggets.count());
		assertEquals("minecraftpp:cychotinte_nugget", nuggets.result());
		assertTrue(RECIPES.containsKey("cychotinte_ingot_from_nuggets"));
		assertEquals(9, ((SmeltingRecipeDefinition) RECIPES.get("cychotinte_nugget_from_smelting")).ingredient().ids().size());
		assertFalse(RECIPES.containsKey("kal_nugget"));
	}

	@Test
	void roleRecipesProduceTheVanillaItems()
	{
		ShapelessRecipeDefinition blueDye = (ShapelessRecipeDefinition) RECIPES.get("blue_dye_from_kal");
		ShapelessRecipeDefinition redstone = (ShapelessRecipeDefinition) RECIPES.get("redstone_from_citi");

		assertEquals("minecraft:blue_dye", blueDye.result());
		assertEquals(6, blueDye.count());
		assertEquals(4, redstone.count());
		assertFalse(RECIPES.keySet().stream().anyMatch(id -> id.startsWith("blue_dye_from_cychotinte")));
	}

	@Test
	void storageBlocksCompactNineItems()
	{
		ShapedRecipeDefinition block = (ShapedRecipeDefinition) RECIPES.get("derepess_block");
		ShapelessRecipeDefinition items = (ShapelessRecipeDefinition) RECIPES.get("derepess_from_derepess_block");

		assertEquals(RecipePatterns.COMPACT, block.pattern());
		assertEquals(9, items.count());
	}
}
