package fr.minecraftpp.content.block;

import fr.minecraftpp.core.set.StorageBlockTraits;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

/**
 * Translates the generated traits into block properties. The hardness and resistance are those of the 1.12 version: 3 and 5 for the ores, 5 and 10 for the storage blocks.
 */
public final class BlockPropertiesFactory
{
	private static final float ORE_HARDNESS = 3.0F;
	private static final float DEEPSLATE_ORE_HARDNESS = 4.5F;
	private static final float ORE_RESISTANCE = 5.0F;
	private static final float STORAGE_BLOCK_HARDNESS = 5.0F;
	private static final float STORAGE_BLOCK_RESISTANCE = 10.0F;

	private BlockPropertiesFactory()
	{
	}

	public static BlockBehaviour.Properties ore()
	{
		return BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(ORE_HARDNESS, ORE_RESISTANCE);
	}

	/**
	 * The deepslate variant is harder to mine, like the vanilla deepslate ores.
	 */
	public static BlockBehaviour.Properties deepslateOre()
	{
		return BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(DEEPSLATE_ORE_HARDNESS, ORE_RESISTANCE).sound(SoundType.DEEPSLATE);
	}

	/**
	 * The slipperiness of 1.12 is the friction of the block, and its acceleration the speed factor of the entities walking on it.
	 */
	public static BlockBehaviour.Properties storageBlock(StorageBlockTraits traits)
	{
		return BlockBehaviour.Properties.of().mapColor(MapColor.EMERALD).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(STORAGE_BLOCK_HARDNESS, STORAGE_BLOCK_RESISTANCE).sound(SoundType.METAL).friction(traits.slipperiness()).speedFactor((float) traits.acceleration()).lightLevel(state -> traits.lightLevel());
	}
}
