package fr.minecraftpp.content.block;

import com.mojang.serialization.MapCodec;

import fr.minecraftpp.core.set.OreDrop;
import fr.minecraftpp.core.set.OreTraits;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * The ore of a generated set, in stone or deepslate. A gem ore drops experience, a metal ore drops itself and no experience, as in 1.12.
 */
public class DynamicOreBlock extends DropExperienceBlock
{
	private final OreTraits traits;
	private final MapCodec<DynamicOreBlock> codec;

	public DynamicOreBlock(BlockBehaviour.Properties properties, OreTraits traits)
	{
		super(experience(traits.drop()), properties);
		this.traits = traits;
		this.codec = simpleCodec(codecProperties -> new DynamicOreBlock(codecProperties, traits));
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
}
