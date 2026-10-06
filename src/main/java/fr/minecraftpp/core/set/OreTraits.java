package fr.minecraftpp.core.set;

import java.util.Objects;

import fr.minecraftpp.core.ore.HarvestLevel;

/**
 * Traits of the ore block of a set.
 *
 * @param powered the ore throws redstone particles when touched, like the vanilla redstone ore
 */
public record OreTraits(int textureId, HarvestLevel harvestLevel, OreDrop drop, boolean powered)
{
	public static final int TEXTURE_COUNT = 4;

	public OreTraits
	{
		Objects.requireNonNull(harvestLevel, "harvestLevel");
		Objects.requireNonNull(drop, "drop");
		Bounds.check("ore texture", textureId, 1, TEXTURE_COUNT);
	}

	/**
	 * The integer average of the dropped quantity, 1 when the ore drops itself.
	 */
	public int averageQuantityDropped()
	{
		if (this.drop instanceof OreDrop.Items items)
		{
			return items.averageQuantity();
		}
		else
		{
			return 1;
		}
	}
}
