package fr.minecraftpp.core.set.generator;

import java.util.Random;

import fr.minecraftpp.core.ore.HarvestLevel;
import fr.minecraftpp.core.set.ItemTraits;
import fr.minecraftpp.core.set.OreGeneration;
import fr.minecraftpp.core.set.OreTraits;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.VanillaRole;
import fr.minecraftpp.core.trait.TraitCatalog;

/**
 * Generates a material set whose item is an ingot with nuggets, and whose ore drops itself. The draws follow the 1.12 MetalSet exactly.
 */
public class MetalSetGenerator extends MaterialSetGenerator
{
	public MetalSetGenerator(String name, OreGeneration generation)
	{
		super(name, generation);
	}

	@Override
	protected SetType type()
	{
		return SetType.METAL;
	}

	/**
	 * A metal set cannot be the currency, and cannot be blue dye or redstone: their recipes would conflict with the nugget recipes.
	 */
	@Override
	protected boolean acceptsRole(VanillaRole role)
	{
		return super.acceptsRole(role) && role != VanillaRole.BLUE_DYE && role != VanillaRole.REDSTONE;
	}

	/**
	 * Same quirk as the material sets: the ore needs a stone pickaxe unless a role changes it.
	 */
	@Override
	protected void constructOre(Random rand, HarvestLevel harvestLevel)
	{
		this.oreTextureId = rand.nextInt(OreTraits.TEXTURE_COUNT) + 1;
		this.oreHarvestLevel = HarvestLevel.STONE;
		this.oreDropsItself = true;
	}

	@Override
	public void setupEffects(Random rand, TraitCatalog traits, GenerationContext context)
	{
		super.setupEffects(rand, traits, context);
		this.itemTextureId = ItemTraits.TEXTURE_COUNT;
	}
}
