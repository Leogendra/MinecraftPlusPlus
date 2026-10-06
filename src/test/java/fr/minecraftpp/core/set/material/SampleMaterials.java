package fr.minecraftpp.core.set.material;

import java.util.Random;

import fr.minecraftpp.core.ore.HarvestLevel;

/**
 * Valid materials for the tests that need one.
 */
public final class SampleMaterials
{
	private SampleMaterials()
	{
	}

	public static MaterialDefinition ironLike()
	{
		return new MaterialDefinition(1, 1, HarvestLevel.IRON, 10, ToolStatsGenerator.generate(new Random(0), 1), ArmorStatsGenerator.generate(new Random(0), 1));
	}

	public static MaterialDefinition withTexture(int textureId)
	{
		MaterialDefinition material = ironLike();
		return new MaterialDefinition(material.tier(), textureId, material.miningLevel(), material.enchantability(), material.tools(), material.armor());
	}
}
