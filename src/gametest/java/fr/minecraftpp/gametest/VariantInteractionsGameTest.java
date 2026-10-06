package fr.minecraftpp.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

/**
 * Decision D7, in the game test seed: ing is the iron set and coms the gold set.
 */
public class VariantInteractionsGameTest
{
	private static final BlockPos GOLEM_FEET = new BlockPos(2, 2, 2);

	@GameTest
	public void theIronGolemIsRepairedWithTheGeneratedIron(GameTestHelper helper)
	{
		IronGolem golem = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, GOLEM_FEET);
		golem.hurtServer(helper.getLevel(), helper.getLevel().damageSources().generic(), 30.0F);
		float hurtHealth = golem.getHealth();

		Player player = helper.makeMockPlayer(GameType.SURVIVAL);
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(GameTestSets.item("ing")));
		golem.interact(player, InteractionHand.MAIN_HAND, golem.position());

		helper.assertTrue(golem.getHealth() > hurtHealth, "the generated iron did not repair the golem");
		helper.succeed();
	}

	@GameTest
	public void piglinsTakeTheGeneratedGoldToBarter(GameTestHelper helper)
	{
		Piglin piglin = helper.spawnWithNoFreeWill(EntityType.PIGLIN, GOLEM_FEET);
		Player player = helper.makeMockPlayer(GameType.SURVIVAL);
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(GameTestSets.item("coms")));

		InteractionResult result = PiglinAi.mobInteract(helper.getLevel(), piglin, player, InteractionHand.MAIN_HAND);

		helper.assertTrue(result.consumesAction(), "the piglin refused the generated gold");
		helper.assertTrue(piglin.getOffhandItem().is(GameTestSets.item("coms")), "the piglin does not hold the generated gold to barter it");
		helper.succeed();
	}

	/**
	 * The vanilla T of four iron blocks, made of generated iron blocks, with a carved pumpkin on top.
	 */
	@GameTest
	public void generatedIronBlocksBuildAnIronGolem(GameTestHelper helper)
	{
		for (BlockPos ironBlock : new BlockPos[] { new BlockPos(2, 1, 2), new BlockPos(1, 2, 2), new BlockPos(2, 2, 2), new BlockPos(3, 2, 2) })
		{
			helper.setBlock(ironBlock, GameTestSets.storageBlock("ing"));
		}

		helper.setBlock(new BlockPos(2, 3, 2), Blocks.CARVED_PUMPKIN);

		helper.succeedWhen(() -> helper.assertEntityPresent(EntityType.IRON_GOLEM));
	}
}
