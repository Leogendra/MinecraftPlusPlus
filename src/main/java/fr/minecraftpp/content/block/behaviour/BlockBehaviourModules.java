package fr.minecraftpp.content.block.behaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import fr.minecraftpp.core.set.OreTraits;
import fr.minecraftpp.core.set.StorageBlockTraits;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The modules of a generated block, chosen from its traits. Each block event is forwarded to every module.
 */
public final class BlockBehaviourModules implements BlockBehaviourModule
{
	private final List<BlockBehaviourModule> modules;

	private BlockBehaviourModules(List<BlockBehaviourModule> modules)
	{
		this.modules = List.copyOf(modules);
	}

	/**
	 * @param walkDamageSource the damage source used when the block hurts the entities walking on it
	 */
	public static BlockBehaviourModules forStorageBlock(StorageBlockTraits traits, Function<Level, DamageSource> walkDamageSource)
	{
		List<BlockBehaviourModule> modules = new ArrayList<>();

		if (traits.falls())
		{
			modules.add(new FallingModule());
		}

		if (traits.absorbsWater())
		{
			modules.add(new AbsorbingModule());
		}

		if (traits.walkDamage() > 0)
		{
			modules.add(new WalkDamageModule(traits.walkDamage(), walkDamageSource));
		}

		if (traits.redstonePower() > 0)
		{
			modules.add(new RedstonePowerModule(traits.redstonePower()));
		}

		return new BlockBehaviourModules(modules);
	}

	public static BlockBehaviourModules forOre(OreTraits traits)
	{
		if (traits.powered())
		{
			return new BlockBehaviourModules(List.of(new PoweredOreModule()));
		}
		else
		{
			return new BlockBehaviourModules(List.of());
		}
	}

	public boolean isSignalSource()
	{
		return this.signal() > 0;
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos)
	{
		this.modules.forEach(module -> module.onPlace(state, level, pos));
	}

	@Override
	public void onNeighborChanged(BlockState state, Level level, BlockPos pos)
	{
		this.modules.forEach(module -> module.onNeighborChanged(state, level, pos));
	}

	@Override
	public void onShapeUpdate(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos)
	{
		this.modules.forEach(module -> module.onShapeUpdate(state, level, ticks, pos));
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		this.modules.forEach(module -> module.tick(state, level, pos, random));
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		this.modules.forEach(module -> module.animateTick(state, level, pos, random));
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity)
	{
		this.modules.forEach(module -> module.stepOn(level, pos, state, entity));
	}

	@Override
	public void onTouched(Level level, BlockPos pos)
	{
		this.modules.forEach(module -> module.onTouched(level, pos));
	}

	@Override
	public int signal()
	{
		return this.modules.stream().mapToInt(BlockBehaviourModule::signal).max().orElse(0);
	}
}
