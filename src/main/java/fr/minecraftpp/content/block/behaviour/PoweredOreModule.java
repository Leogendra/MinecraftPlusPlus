package fr.minecraftpp.content.block.behaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The ore of the redstone set throws redstone particles when it is placed, walked on, used or hit, as in 1.12. The particles appear on the free faces of the ore.
 */
public class PoweredOreModule implements BlockBehaviourModule
{
	private static final double FACE_OFFSET = 0.0625;

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos)
	{
		spawnParticles(level, pos);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity)
	{
		spawnParticles(level, pos);
	}

	@Override
	public void onTouched(Level level, BlockPos pos)
	{
		spawnParticles(level, pos);
	}

	/**
	 * Sent by the server, so that every player near the ore sees the particles.
	 */
	private static void spawnParticles(Level level, BlockPos pos)
	{
		if (level instanceof ServerLevel serverLevel)
		{
			RandomSource random = level.getRandom();

			for (Direction direction : Direction.values())
			{
				if (!level.getBlockState(pos.relative(direction)).isSolidRender())
				{
					double x = coordinate(pos.getX(), direction.getStepX(), random);
					double y = coordinate(pos.getY(), direction.getStepY(), random);
					double z = coordinate(pos.getZ(), direction.getStepZ(), random);
					serverLevel.sendParticles(DustParticleOptions.REDSTONE, x, y, z, 1, 0, 0, 0, 0);
				}
			}
		}
	}

	private static double coordinate(int blockCoordinate, int step, RandomSource random)
	{
		if (step == 0)
		{
			return blockCoordinate + random.nextDouble();
		}
		else if (step > 0)
		{
			return blockCoordinate + 1 + FACE_OFFSET;
		}
		else
		{
			return blockCoordinate - FACE_OFFSET;
		}
	}
}
