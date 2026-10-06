package fr.minecraftpp.content.block.behaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.state.BlockState;

/**
 * One behaviour a generated block can receive from its traits. The generated blocks compose their modules instead of inheriting a fixed behaviour; each module only overrides the block events it needs.
 */
public interface BlockBehaviourModule
{
	default void onPlace(BlockState state, Level level, BlockPos pos)
	{
	}

	default void onNeighborChanged(BlockState state, Level level, BlockPos pos)
	{
	}

	default void onShapeUpdate(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos)
	{
	}

	default void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
	}

	default void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
	}

	default void stepOn(Level level, BlockPos pos, BlockState state, Entity entity)
	{
	}

	/**
	 * A player used or hit the block.
	 */
	default void onTouched(Level level, BlockPos pos)
	{
	}

	/**
	 * The redstone signal emitted on every side, 0 when the module emits none.
	 */
	default int signal()
	{
		return 0;
	}
}
