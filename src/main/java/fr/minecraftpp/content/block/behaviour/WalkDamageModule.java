package fr.minecraftpp.content.block.behaviour;

import java.util.function.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The block hurts the entities walking on it. Unlike the vanilla magma block, sneaking does not protect, as in 1.12.
 */
public class WalkDamageModule implements BlockBehaviourModule
{
	private final float damage;
	private final Function<Level, DamageSource> damageSource;

	/**
	 * @param damageSource the damage source of the level, whose death message names the block
	 */
	public WalkDamageModule(float damage, Function<Level, DamageSource> damageSource)
	{
		this.damage = damage;
		this.damageSource = damageSource;
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity)
	{
		if (level instanceof ServerLevel serverLevel)
		{
			entity.hurtServer(serverLevel, this.damageSource.apply(level), this.damage);
		}
	}
}
