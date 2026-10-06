package fr.minecraftpp.content.block.behaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The block removes the water around it, like a sponge that never gets wet. The search is the one of the vanilla sponge: up to 6 blocks away and 64 blocks of water.
 */
public class AbsorbingModule implements BlockBehaviourModule
{
	private static final int MAX_DEPTH = 6;
	private static final int MAX_COUNT = 65;

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos)
	{
		this.absorb(level, pos);
	}

	@Override
	public void onNeighborChanged(BlockState state, Level level, BlockPos pos)
	{
		this.absorb(level, pos);
	}

	private void absorb(Level level, BlockPos pos)
	{
		if (!level.isClientSide() && this.removeWater(level, pos))
		{
			level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(Blocks.WATER.defaultBlockState()));
		}
	}

	private boolean removeWater(Level level, BlockPos startPos)
	{
		int visited = BlockPos.breadthFirstTraversal(startPos, MAX_DEPTH, MAX_COUNT, (pos, consumer) -> {
			for (Direction direction : Direction.values())
			{
				consumer.accept(pos.relative(direction));
			}
		}, pos -> pos.equals(startPos) ? BlockPos.TraversalNodeStatus.ACCEPT : removeWaterAt(level, pos));

		return visited > 1;
	}

	private static BlockPos.TraversalNodeStatus removeWaterAt(Level level, BlockPos pos)
	{
		BlockState state = level.getBlockState(pos);

		if (!level.getFluidState(pos).is(FluidTags.WATER))
		{
			return BlockPos.TraversalNodeStatus.SKIP;
		}
		else if (state.getBlock() instanceof BucketPickup bucketPickup && !bucketPickup.pickupBlock(null, level, pos, state).isEmpty())
		{
			return BlockPos.TraversalNodeStatus.ACCEPT;
		}
		else if (state.getBlock() instanceof LiquidBlock)
		{
			level.setBlock(pos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
			return BlockPos.TraversalNodeStatus.ACCEPT;
		}
		else
		{
			return BlockPos.TraversalNodeStatus.SKIP;
		}
	}
}
