package fr.minecraftpp.gametest;

import fr.minecraftpp.MinecraftPlusPlus;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreSetDefinition;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The game tests run with seed 42 (see writeGameTestSeed in build.gradle).
 */
public class BlockPlacementGameTest
{
	private static final BlockPos POSITION = new BlockPos(1, 1, 1);

	@GameTest
	public void everySetHasItsThreeBlocks(GameTestHelper helper)
	{
		long registeredBlocks = BuiltInRegistries.BLOCK.keySet().stream().filter(id -> id.getNamespace().equals(ContentIds.NAMESPACE)).count();

		helper.assertTrue(registeredBlocks == 21, "expected 21 blocks, got " + registeredBlocks);
		helper.succeed();
	}

	/**
	 * The study before the migration found that registering blocks could shift the vanilla state identifiers: air must keep identifier 0, and every state must find its identifier back.
	 */
	@GameTest
	public void blockStateIdentifiersStayConsistent(GameTestHelper helper)
	{
		helper.assertTrue(Block.getId(Blocks.AIR.defaultBlockState()) == 0, "air must keep the state identifier 0");

		for (Block block : MinecraftPlusPlus.content().blocks())
		{
			for (BlockState state : block.getStateDefinition().getPossibleStates())
			{
				helper.assertTrue(Block.stateById(Block.getId(state)) == state, "state identifier round trip failed for " + state);
			}
		}

		helper.succeed();
	}

	@GameTest
	public void placedStorageBlocksEmitTheirLight(GameTestHelper helper)
	{
		for (OreSetDefinition set : MinecraftPlusPlus.catalog().sets())
		{
			helper.setBlock(POSITION, MinecraftPlusPlus.content().block(ContentIds.storageBlock(set)));
			int emittedLight = helper.getBlockState(POSITION).getLightEmission();

			helper.assertTrue(emittedLight == set.block().lightLevel(), set.name() + " block emits " + emittedLight + " instead of " + set.block().lightLevel());
		}

		helper.succeed();
	}
}
