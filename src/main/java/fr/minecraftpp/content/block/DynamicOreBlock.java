package fr.minecraftpp.content.block;

import com.mojang.serialization.MapCodec;

import fr.minecraftpp.content.block.behaviour.BlockBehaviourModules;
import fr.minecraftpp.core.set.OreDrop;
import fr.minecraftpp.core.set.OreTraits;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * The ore of a generated set, in stone or deepslate. A gem ore drops experience, a metal ore drops itself and no experience, as in 1.12. The ore of the redstone set throws redstone particles when touched.
 */
public class DynamicOreBlock extends DropExperienceBlock
{
	private final OreTraits traits;
	private final BlockBehaviourModules modules;
	private final MapCodec<DynamicOreBlock> codec;

	public DynamicOreBlock(BlockBehaviour.Properties properties, OreTraits traits, BlockBehaviourModules modules)
	{
		super(experience(traits.drop()), properties);
		this.traits = traits;
		this.modules = modules;
		this.codec = simpleCodec(codecProperties -> new DynamicOreBlock(codecProperties, traits, modules));
	}

	private static IntProvider experience(OreDrop drop)
	{
		if (drop instanceof OreDrop.Items items)
		{
			return UniformInt.of(items.minimumExperience(), items.maximumExperience());
		}
		else
		{
			return ConstantInt.ZERO;
		}
	}

	public OreTraits getTraits()
	{
		return this.traits;
	}

	@Override
	public MapCodec<? extends DynamicOreBlock> codec()
	{
		return this.codec;
	}

	@Override
	protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
	{
		this.modules.onPlace(state, level, pos);
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity)
	{
		this.modules.stepOn(level, pos, onState, entity);
		super.stepOn(level, pos, onState, entity);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
	{
		this.modules.onTouched(level, pos);
		return super.useWithoutItem(state, level, pos, player, hitResult);
	}

	@Override
	protected void attack(BlockState state, Level level, BlockPos pos, Player player)
	{
		this.modules.onTouched(level, pos);
		super.attack(state, level, pos, player);
	}
}
