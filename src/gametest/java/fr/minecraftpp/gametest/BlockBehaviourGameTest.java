package fr.minecraftpp.gametest;

import fr.minecraftpp.content.block.behaviour.AbsorbingModule;
import fr.minecraftpp.content.block.behaviour.FallingModule;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.block.Blocks;

/**
 * No set of the game test seed falls or absorbs water: these modules are tested on their own, the others on the generated blocks.
 */
public class BlockBehaviourGameTest
{
	private static final BlockPos POSITION = new BlockPos(2, 3, 2);

	@GameTest
	public void fallingModuleDropsAnUnsupportedBlock(GameTestHelper helper)
	{
		helper.setBlock(POSITION, Blocks.STONE);

		new FallingModule().tick(Blocks.STONE.defaultBlockState(), helper.getLevel(), helper.absolutePos(POSITION), helper.getLevel().getRandom());

		helper.assertBlockNotPresent(Blocks.STONE, POSITION);
		helper.succeed();
	}

	@GameTest
	public void redstoneBlockEmitsAFullSignal(GameTestHelper helper)
	{
		helper.setBlock(POSITION, GameTestSets.storageBlock("novic"));
		int signal = helper.getLevel().getBestNeighborSignal(helper.absolutePos(POSITION.above()));

		helper.assertTrue(signal == 15, "expected a signal of 15, got " + signal);
		helper.succeed();
	}

	@GameTest
	public void absorbingModuleRemovesTheWaterAround(GameTestHelper helper)
	{
		helper.setBlock(POSITION, Blocks.STONE);
		helper.setBlock(POSITION.east(), Blocks.WATER);
		helper.setBlock(POSITION.north(), Blocks.WATER);

		new AbsorbingModule().onPlace(Blocks.STONE.defaultBlockState(), helper.getLevel(), helper.absolutePos(POSITION));

		helper.assertBlockNotPresent(Blocks.WATER, POSITION.east());
		helper.assertBlockNotPresent(Blocks.WATER, POSITION.north());
		helper.succeed();
	}

	@GameTest
	public void walkingOnTheBlockHurts(GameTestHelper helper)
	{
		helper.setBlock(POSITION, GameTestSets.storageBlock("voging"));
		Pig pig = helper.spawnWithNoFreeWill(EntityType.PIG, POSITION.above());
		float maximumHealth = pig.getMaxHealth();

		helper.succeedWhen(() -> helper.assertTrue(pig.getHealth() < maximumHealth, "the pig standing on the block is not hurt"));
	}
}
