package fr.minecraftpp.core.set;

import java.util.Objects;

import fr.minecraftpp.core.ore.FlammabilityOf;
import fr.minecraftpp.core.ore.HarvestLevel;

/**
 * Traits of the storage block of a set, crafted from nine items.
 *
 * @param walkDamage    damage dealt to an entity walking on the block, 0 when harmless
 * @param acceleration  speed factor of an entity walking on the block, 1 when neutral
 * @param redstonePower redstone signal emitted by the block, 0 when the block is not a power source
 * @param lightOpacity  light absorbed by the block, from 0 (transparent) to 255 (opaque)
 * @param slipperiness  friction of the block, 0.6 for most vanilla blocks
 */
public record StorageBlockTraits(int textureId, HarvestLevel harvestLevel, boolean falls, boolean absorbsWater, float walkDamage, FlammabilityOf flammability, double acceleration, boolean beaconBase, int redstonePower, int fuelTicks, int lightLevel, int lightOpacity, float slipperiness)
{
	public static final int TEXTURE_COUNT = 4;
	public static final float DEFAULT_SLIPPERINESS = 0.6F;
	public static final int OPAQUE = 255;

	public StorageBlockTraits
	{
		Objects.requireNonNull(harvestLevel, "harvestLevel");
		Objects.requireNonNull(flammability, "flammability");
		Bounds.check("block texture", textureId, 1, TEXTURE_COUNT);
		Bounds.check("walk damage", walkDamage, 0, Float.MAX_VALUE);
		Bounds.check("acceleration", acceleration, 0, Double.MAX_VALUE);
		Bounds.check("redstone power", redstonePower, 0, 15);
		Bounds.check("block fuel ticks", fuelTicks, 0, Integer.MAX_VALUE);
		Bounds.check("light level", lightLevel, 0, 15);
		Bounds.check("light opacity", lightOpacity, 0, OPAQUE);
		Bounds.check("slipperiness", slipperiness, 0, 1);
	}
}
