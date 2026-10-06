package fr.minecraftpp.content.block.behaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The block falls when nothing holds it, like sand.
 */
public class FallingModule implements BlockBehaviourModule
{
	private static final int DELAY_AFTER_PLACE = 2;

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos)
	{
		level.scheduleTick(pos, state.getBlock(), DELAY_AFTER_PLACE);
	}

	@Override
	public void onShapeUpdate(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos)
	{
		ticks.scheduleTick(pos, state.getBlock(), DELAY_AFTER_PLACE);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		if (FallingBlock.isFree(level.getBlockState(pos.below())) && pos.getY() >= level.getMinY())
		{
			FallingBlockEntity.fall(level, pos, state);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		if (random.nextInt(16) == 0 && FallingBlock.isFree(level.getBlockState(pos.below())))
		{
			ParticleUtils.spawnParticleBelow(level, pos, random, new BlockParticleOption(ParticleTypes.FALLING_DUST, state));
		}
	}
}
