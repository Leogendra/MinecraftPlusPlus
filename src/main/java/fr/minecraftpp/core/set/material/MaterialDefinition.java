package fr.minecraftpp.core.set.material;

import java.util.Objects;

import fr.minecraftpp.core.ore.HarvestLevel;
import fr.minecraftpp.core.set.Bounds;

/**
 * Tool and armor material of a material or metal set.
 *
 * @param tier        quality of the material, from 0 to 2, which scales its statistics
 * @param textureId   index of the generic_N armor texture, 1 or 2
 * @param miningLevel the hardest blocks the tools can mine
 */
public record MaterialDefinition(int tier, int textureId, HarvestLevel miningLevel, int enchantability, ToolStats tools, ArmorStats armor)
{
	public static final int TEXTURE_COUNT = 2;

	public MaterialDefinition
	{
		Objects.requireNonNull(miningLevel, "miningLevel");
		Objects.requireNonNull(tools, "tools");
		Objects.requireNonNull(armor, "armor");
		Bounds.check("tier", tier, 0, 2);
		Bounds.check("material texture", textureId, 1, TEXTURE_COUNT);
		Bounds.check("enchantability", enchantability, 1, Integer.MAX_VALUE);
	}
}
