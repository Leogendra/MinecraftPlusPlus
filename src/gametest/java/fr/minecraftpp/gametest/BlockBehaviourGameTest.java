package fr.minecraftpp.gametest;

import fr.minecraftpp.MinecraftPlusPlus;
import fr.minecraftpp.content.block.behaviour.AbsorbingModule;
import fr.minecraftpp.content.block.behaviour.WalkDamageModule;
import fr.minecraftpp.core.set.ContentIds;
import fr.minecraftpp.core.set.OreSetDefinition;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

/**
 * Seed 42: dium falls, citi is the redstone set. No set of seed 42 absorbs water or hurts, so these modules are tested on their own.
 */
public class BlockBehaviourGameTest
{
	private static final BlockPos POSITION = new BlockPos(2, 3, 2);

	@GameTest
	public void blockWithoutSupportFalls(GameTestHelper helper)
	{
		Block block = storageBlockOf("dium");
		helper.setBlock(POSITION, block);

		helper.succeedWhen(() -> helper.assertBlockNotPresent(block, POSITION));
	}

	@GameTest
	public void redstoneBlockEmitsAFullSignal(GameTestHelper helper)
	{
		helper.setBlock(POSITION, storageBlockOf("citi"));
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
	public void walkDamageModuleHurtsTheEntityOnTheBlock(GameTestHelper helper)
	{
		Pig pig = helper.spawnWithNoFreeWill(EntityType.PIG, POSITION.above());
		float healthBefore = pig.getHealth();

		new WalkDamageModule(2.0F, level -> level.damageSources().hotFloor()).stepOn(helper.getLevel(), helper.absolutePos(POSITION), Blocks.STONE.defaultBlockState(), pig);

		helper.assertTrue(pig.getHealth() == healthBefore - 2.0F, "the pig should have lost 2 health points");
		helper.succeed();
	}

	private static Block storageBlockOf(String setName)
	{
		OreSetDefinition set = MinecraftPlusPlus.catalog().sets().stream().filter(candidate -> candidate.name().equals(setName)).findFirst().orElseThrow(() -> new IllegalStateException("The game tests expect seed 42, which has no set " + setName));
		return MinecraftPlusPlus.content().block(ContentIds.storageBlock(set));
	}
}
