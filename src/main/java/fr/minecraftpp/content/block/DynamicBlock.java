package fr.minecraftpp.content.block;

import com.mojang.serialization.MapCodec;

import fr.minecraftpp.core.set.StorageBlockTraits;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * The storage block of a generated set, crafted from nine items.
 */
public class DynamicBlock extends Block
{
	private final StorageBlockTraits traits;
	private final MapCodec<DynamicBlock> codec;

	public DynamicBlock(BlockBehaviour.Properties properties, StorageBlockTraits traits)
	{
		super(properties);
		this.traits = traits;
		this.codec = simpleCodec(codecProperties -> new DynamicBlock(codecProperties, traits));
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
}
