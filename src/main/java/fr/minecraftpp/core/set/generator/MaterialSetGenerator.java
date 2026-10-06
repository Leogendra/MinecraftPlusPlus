package fr.minecraftpp.core.set.generator;

import java.util.Optional;
import java.util.Random;

import fr.minecraftpp.core.ore.HarvestLevel;
import fr.minecraftpp.core.set.OreGeneration;
import fr.minecraftpp.core.set.OreTraits;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.VanillaRole;
import fr.minecraftpp.core.set.material.ArmorStats;
import fr.minecraftpp.core.set.material.ArmorStatsGenerator;
import fr.minecraftpp.core.set.material.MaterialDefinition;
import fr.minecraftpp.core.set.material.ToolStats;
import fr.minecraftpp.core.set.material.ToolStatsGenerator;

/**
 * Generates a simple set plus a tool and armor material. The draws follow the 1.12 MaterialSet exactly.
 */
public class MaterialSetGenerator extends SimpleSetGenerator
{
	private int materialTextureId;
	private int tier;
	private HarvestLevel miningLevel;
	private int enchantability;
	private ToolStats tools;
	private ArmorStats armor;

	public MaterialSetGenerator(String name, OreGeneration generation)
	{
		super(name, generation);
	}

	@Override
	protected SetType type()
	{
		return SetType.MATERIAL;
	}

	/**
	 * A material set cannot be the currency: crafting tools and armor from the currency is not desired.
	 */
	@Override
	protected boolean acceptsRole(VanillaRole role)
	{
		return role != VanillaRole.CURRENCY;
	}

	@Override
	public void construct(Random rand)
	{
		super.construct(rand);

		this.materialTextureId = rand.nextInt(MaterialDefinition.TEXTURE_COUNT) + 1;
		this.tier = rand.nextInt(3);
		this.armor = ArmorStatsGenerator.generate(rand, this.tier);
		this.tools = ToolStatsGenerator.generate(rand, this.tier);
		this.miningLevel = HarvestLevel.values()[this.tier + 1];
		this.enchantability = rand.nextInt(18) + 8;
	}

	/**
	 * In 1.12 the ore was created before the tier was drawn, while the tier still held its default value of 0: material ores always need a stone pickaxe, unless a role changes it.
	 *
	 * The 1.12 tier also checked the iron, gold and diamond roles, but the roles were assigned after the construction, so the tier is always drawn.
	 */
	@Override
	protected void constructOre(Random rand, HarvestLevel harvestLevel)
	{
		this.oreTextureId = rand.nextInt(OreTraits.TEXTURE_COUNT) + 1;
		this.oreHarvestLevel = HarvestLevel.STONE;
		this.drawExperience(rand);
	}

	@Override
	protected void applyRole(VanillaRole role, Random rand, GenerationContext context)
	{
		super.applyRole(role, rand, context);

		switch (role)
		{
			case IRON, GOLD -> this.miningLevel = HarvestLevel.IRON;
			case DIAMOND -> this.miningLevel = HarvestLevel.DIAMOND;
			case COPPER -> this.miningLevel = HarvestLevel.STONE;
			default ->
			{
				// The other roles do not change the material
			}
		}
	}

	@Override
	protected Optional<MaterialDefinition> material()
	{
		return Optional.of(new MaterialDefinition(this.tier, this.materialTextureId, this.miningLevel, this.enchantability, this.tools, this.armor));
	}
}
