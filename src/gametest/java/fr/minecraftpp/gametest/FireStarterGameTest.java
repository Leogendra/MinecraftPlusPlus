package fr.minecraftpp.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * novic, the redstone set of the game test seed, lights fires.
 */
public class FireStarterGameTest
{
	private static final BlockPos GROUND = new BlockPos(2, 1, 2);

	@GameTest
	public void firestarterLightsAFireAndIsConsumed(GameTestHelper helper)
	{
		helper.setBlock(GROUND, Blocks.STONE);
		Player player = helper.makeMockPlayer(GameType.SURVIVAL);
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(GameTestSets.item("novic"), 2));

		BlockPos ground = helper.absolutePos(GROUND);
		BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(ground), Direction.UP, ground, false);
		player.getMainHandItem().useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hit));

		helper.assertBlockPresent(Blocks.FIRE, GROUND.above());
		helper.assertTrue(player.getMainHandItem().getCount() == 1, "the firestarter should be consumed");
		helper.succeed();
	}
}
