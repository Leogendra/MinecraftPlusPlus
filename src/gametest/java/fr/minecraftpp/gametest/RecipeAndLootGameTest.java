package fr.minecraftpp.gametest;

import java.util.List;
import java.util.Optional;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * In the game test seed, brumed (gem ore) and ing (metal ore) are smelted; brumed needs an iron tool, imer a stone tool and drops two items, voging a wooden tool; coms is a beacon set, imer is not.
 */
public class RecipeAndLootGameTest
{
	@GameTest
	public void smeltingAnOreGivesItsItem(GameTestHelper helper)
	{
		for (String setName : new String[] { "brumed", "ing" })
		{
			SingleRecipeInput ore = new SingleRecipeInput(new ItemStack(GameTestSets.ore(setName)));
			Optional<RecipeHolder<SmeltingRecipe>> recipe = helper.getLevel().getServer().getRecipeManager().getRecipeFor(RecipeType.SMELTING, ore, helper.getLevel());

			helper.assertTrue(recipe.isPresent(), "no smelting recipe for the " + setName + " ore");
			helper.assertTrue(recipe.get().value().assemble(ore).is(GameTestSets.item(setName)), "the " + setName + " ore smelts into " + recipe.get().value().assemble(ore));
		}

		helper.succeed();
	}

	@GameTest
	public void oresNeedTheirToolLevel(GameTestHelper helper)
	{
		assertCorrectTool(helper, GameTestSets.ore("brumed"), new ItemStack(Items.STONE_PICKAXE), false);
		assertCorrectTool(helper, GameTestSets.ore("brumed"), new ItemStack(Items.IRON_PICKAXE), true);
		assertCorrectTool(helper, GameTestSets.ore("imer"), new ItemStack(Items.WOODEN_PICKAXE), false);
		assertCorrectTool(helper, GameTestSets.ore("imer"), new ItemStack(Items.STONE_PICKAXE), true);
		assertCorrectTool(helper, GameTestSets.ore("voging"), new ItemStack(Items.WOODEN_PICKAXE), true);
		assertCorrectTool(helper, GameTestSets.ore("voging"), new ItemStack(Items.WOODEN_SHOVEL), false);
		helper.succeed();
	}

	@GameTest
	public void oresAndBlocksDropTheirLoot(GameTestHelper helper)
	{
		ItemStack pickaxe = new ItemStack(Items.DIAMOND_PICKAXE);

		assertDrops(helper, GameTestSets.ore("imer"), pickaxe, new ItemStack(GameTestSets.item("imer"), 2));
		assertDrops(helper, GameTestSets.ore("ing"), pickaxe, new ItemStack(GameTestSets.ore("ing")));
		assertDrops(helper, GameTestSets.storageBlock("coms"), pickaxe, new ItemStack(GameTestSets.storageBlock("coms")));
		helper.succeed();
	}

	@GameTest
	public void beaconSetsFeedTheBeacon(GameTestHelper helper)
	{
		helper.assertTrue(GameTestSets.storageBlock("coms").defaultBlockState().is(BlockTags.BEACON_BASE_BLOCKS), "coms is not a beacon base");
		helper.assertFalse(GameTestSets.storageBlock("imer").defaultBlockState().is(BlockTags.BEACON_BASE_BLOCKS), "imer is a beacon base");
		helper.assertTrue(new ItemStack(GameTestSets.item("coms")).is(ItemTags.BEACON_PAYMENT_ITEMS), "coms does not pay the beacon");
		helper.assertFalse(new ItemStack(GameTestSets.item("imer")).is(ItemTags.BEACON_PAYMENT_ITEMS), "imer pays the beacon");
		helper.succeed();
	}

	private static void assertCorrectTool(GameTestHelper helper, Block block, ItemStack tool, boolean expected)
	{
		BlockState state = block.defaultBlockState();

		helper.assertTrue(tool.isCorrectToolForDrops(state) == expected, tool + " on " + block + " should be " + (expected ? "correct" : "too weak"));
	}

	private static void assertDrops(GameTestHelper helper, Block block, ItemStack tool, ItemStack expected)
	{
		List<ItemStack> drops = Block.getDrops(block.defaultBlockState(), helper.getLevel(), helper.absolutePos(BlockPos.ZERO), null, null, tool);

		helper.assertTrue(drops.size() == 1 && ItemStack.matches(drops.getFirst(), expected), block + " drops " + drops + " instead of " + expected);
	}
}
