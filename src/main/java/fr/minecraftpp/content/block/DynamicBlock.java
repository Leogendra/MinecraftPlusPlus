package fr.minecraftpp.content.block;

import org.jspecify.annotations.Nullable;

import com.mojang.serialization.MapCodec;

import fr.minecraftpp.content.block.behaviour.BlockBehaviourModules;
import fr.minecraftpp.core.set.StorageBlockTraits;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;

/**
 * The storage block of a generated set, crafted from nine items. Its behaviours come from its traits (falling, absorbing water, walk damage, redstone power).
 */
public class DynamicBlock extends Block
{
	private static final int MAX_LIGHT_DAMPENING = 15;

	private final StorageBlockTraits traits;
	private final BlockBehaviourModules modules;
	private final MapCodec<DynamicBlock> codec;

	public DynamicBlock(BlockBehaviour.Properties properties, StorageBlockTraits traits, BlockBehaviourModules modules)
	{
		super(properties);
		this.traits = traits;
		this.modules = modules;
		this.codec = simpleCodec(codecProperties -> new DynamicBlock(codecProperties, traits, modules));
	}

	public StorageBlockTraits getTraits()
	{
		return this.traits;
	}

	@Override
	protected MapCodec<? extends Block> codec()
	{
		return this.codec;
	}

	/**
	 * In 1.12 the opacity went from 0 to 255, but light never goes above 15: only an opacity below 15 lets some light through.
	 */
	@Override
	protected int getLightDampening(BlockState state)
	{
		return Math.min(this.traits.lightOpacity(), MAX_LIGHT_DAMPENING);
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
	{
		this.modules.onPlace(state, level, pos);
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean movedByPiston)
	{
		this.modules.onNeighborChanged(state, level, pos);
		super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random)
	{
		this.modules.onShapeUpdate(state, level, ticks, pos);
		return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		this.modules.tick(state, level, pos, random);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		this.modules.animateTick(state, level, pos, random);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity)
	{
		this.modules.stepOn(level, pos, onState, entity);
		super.stepOn(level, pos, onState, entity);
	}

	@Override
	protected boolean isSignalSource(BlockState state)
	{
		return this.modules.isSignalSource();
	}

	@Override
	protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
	{
		return this.modules.signal();
	}
}
