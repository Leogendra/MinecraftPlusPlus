package fr.minecraftpp.gametest;

import java.util.List;
import java.util.Optional;

import fr.minecraftpp.MinecraftPlusPlus;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * In the game test seed, ing is the iron set and voging the coal set: their items replace the vanilla ones in the vanilla recipes.
 */
public class VariantCraftingGameTest
{
	@GameTest
	public void aBucketIsCraftedWithTheGeneratedIron(GameTestHelper helper)
	{
		ItemStack ingot = new ItemStack(GameTestSets.item("ing"));

		assertCrafts(helper, CraftingInput.of(3, 2, List.of(ingot, ItemStack.EMPTY, ingot, ItemStack.EMPTY, ingot, ItemStack.EMPTY)), Items.BUCKET);
		helper.succeed();
	}

	@GameTest
	public void torchesAreCraftedWithTheGeneratedCoal(GameTestHelper helper)
	{
		assertCrafts(helper, CraftingInput.of(1, 2, List.of(new ItemStack(GameTestSets.item("voging")), new ItemStack(Items.STICK))), Items.TORCH);
		helper.succeed();
	}

	/**
	 * Regression: a rewritten recipe the game cannot read is only logged, and the vanilla recipe silently disappears.
	 */
	@GameTest
	public void theGameReadsEveryRewrittenVanillaRecipe(GameTestHelper helper)
	{
		RecipeManager recipes = helper.getLevel().getServer().getRecipeManager();

		for (Identifier location : MinecraftPlusPlus.packContents().resources(PackType.SERVER_DATA, Identifier.DEFAULT_NAMESPACE, "recipe").keySet())
		{
			Identifier recipeId = location.withPath(path -> path.substring("recipe/".length(), path.length() - ".json".length()));

			helper.assertTrue(recipes.byKey(ResourceKey.create(Registries.RECIPE, recipeId)).isPresent(), "the game did not load the rewritten recipe " + recipeId);
		}

		helper.succeed();
	}

	private static void assertCrafts(GameTestHelper helper, CraftingInput input, Item expected)
	{
		Optional<RecipeHolder<CraftingRecipe>> recipe = helper.getLevel().getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel());

		helper.assertTrue(recipe.isPresent(), "no recipe crafts " + expected + " from " + input.items());
		helper.assertTrue(recipe.get().value().assemble(input).is(expected), input.items() + " crafts " + recipe.get().value().assemble(input) + " instead of " + expected);
	}
}
