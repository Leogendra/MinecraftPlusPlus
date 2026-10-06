package fr.minecraftpp.core.set.generator;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import fr.minecraftpp.core.ore.Color;
import fr.minecraftpp.core.ore.FlammabilityOf;
import fr.minecraftpp.core.ore.HarvestLevel;
import fr.minecraftpp.core.ore.Rarity;
import fr.minecraftpp.core.set.FoodDefinition;
import fr.minecraftpp.core.set.ItemTraits;
import fr.minecraftpp.core.set.OreDrop;
import fr.minecraftpp.core.set.OreGeneration;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.set.OreTraits;
import fr.minecraftpp.core.set.SetType;
import fr.minecraftpp.core.set.StorageBlockTraits;
import fr.minecraftpp.core.set.VanillaRole;
import fr.minecraftpp.core.set.material.MaterialDefinition;
import fr.minecraftpp.core.trait.Trait;
import fr.minecraftpp.core.trait.TraitCatalog;

/**
 * Generates a set made of an ore, an item and a storage block. The draws follow the 1.12 SimpleSet exactly.
 */
public class SimpleSetGenerator implements OreSetGenerator
{
	private static final int REDSTONE_BLOCK_POWER = 15;
	private static final int FUEL_TICKS_PER_LEVEL = 200;
	private static final int BLOCK_FUEL_MULTIPLIER = 10;
	private static final int FULL_LIGHT = 15;

	protected final String name;
	private final OreGeneration generation;
	private final Set<VanillaRole> roles = EnumSet.noneOf(VanillaRole.class);

	protected Rarity rarity = Rarity.COMMON;

	protected int itemTextureId;
	private Color color;
	private boolean shiny;
	private int itemFuelTicks;
	private boolean enchantingCurrency;
	private boolean firestarter;
	private boolean beaconPayment;
	private FoodDefinition food;

	private int blockTextureId;
	private HarvestLevel blockHarvestLevel;
	private boolean falls;
	private boolean absorbsWater;
	private float walkDamage;
	private FlammabilityOf flammability = FlammabilityOf.STONE;
	private double acceleration = 1;
	private boolean beaconBase;
	private int redstonePower;
	private int blockFuelTicks;
	private int lightLevel;
	private int lightOpacity = StorageBlockTraits.OPAQUE;
	private float slipperiness = StorageBlockTraits.DEFAULT_SLIPPERINESS;

	protected int oreTextureId;
	protected HarvestLevel oreHarvestLevel;
	protected boolean oreDropsItself;
	private int minimumDrop = 1;
	private int maximumDrop = 1;
	private int minimumExperience;
	private int maximumExperience;
	private boolean powered;

	public SimpleSetGenerator(String name, OreGeneration generation)
	{
		this.name = name;
		this.generation = generation;
	}

	protected SetType type()
	{
		return SetType.SIMPLE;
	}

	/**
	 * Whether the set keeps a role handed out by the solver. The 1.12 material and metal sets silently ignored some roles.
	 */
	protected boolean acceptsRole(VanillaRole role)
	{
		return true;
	}

	/**
	 * Gives a role handed out by the solver to the set, unless the set ignores it. As in 1.12, the roles are assigned after the construction pass and do not change it.
	 */
	public void assignRole(VanillaRole role)
	{
		if (this.acceptsRole(role))
		{
			this.roles.add(role);
		}
	}

	@Override
	public void construct(Random rand)
	{
		this.itemTextureId = rand.nextInt(ItemTraits.TEXTURE_COUNT - 1) + 1;
		this.color = Color.getRandomColorImproved(rand);

		HarvestLevel harvestLevel = HarvestLevel.getRandomHarvestLevel(rand);
		this.blockTextureId = rand.nextInt(StorageBlockTraits.TEXTURE_COUNT) + 1;
		this.blockHarvestLevel = harvestLevel;

		this.constructOre(rand, harvestLevel);
	}

	protected void constructOre(Random rand, HarvestLevel harvestLevel)
	{
		this.oreTextureId = rand.nextInt(OreTraits.TEXTURE_COUNT) + 1;
		this.oreHarvestLevel = harvestLevel;
		this.drawExperience(rand);
	}

	protected void drawExperience(Random rand)
	{
		this.minimumExperience = rand.nextInt(2);
		this.maximumExperience = rand.nextInt(3) + 2;
	}

	@Override
	public void setupEffects(Random rand, TraitCatalog traits, GenerationContext context)
	{
		this.drawItemTraits(rand, traits);
		this.drawBlockTraits(rand, traits);

		for (VanillaRole role : VanillaRole.values())
		{
			if (this.roles.contains(role))
			{
				this.applyRole(role, rand, context);
			}
		}
	}

	private void drawItemTraits(Random rand, TraitCatalog traits)
	{
		this.shiny = traits.draw(Trait.SHINY, rand);
		this.firestarter = traits.draw(Trait.FIRESTARTER, rand);

		if (traits.draw(Trait.EDIBLE, rand))
		{
			this.food = drawFood(rand);
		}
	}

	private static FoodDefinition drawFood(Random rand)
	{
		int nutrition = 1 + rand.nextInt(5) + (rand.nextInt(2) == 0 ? rand.nextInt(5) : 0);
		float saturation = Math.max(0, nutrition - ((2 * rand.nextFloat()) - 1));
		boolean wolfFood = rand.nextInt(5) == 0;

		return new FoodDefinition(nutrition, saturation, wolfFood, false);
	}

	/**
	 * Some 1.12 formulas divide integers: half of the glowing blocks get no light, the slippery blocks always get 0.4 and the accelerating blocks 0.5 or 1.5. They are kept as is so that a seed gives the same sets as in 1.12.
	 */
	private void drawBlockTraits(Random rand, TraitCatalog traits)
	{
		this.falls = traits.draw(Trait.FALLS, rand);

		if (traits.draw(Trait.GLOWING, rand))
		{
			this.lightLevel = FULL_LIGHT * (1 / (rand.nextInt(2) + 1));
		}

		this.absorbsWater = traits.draw(Trait.ABSORBS_WATER, rand);

		if (traits.draw(Trait.TRANSLUCENT, rand))
		{
			this.lightOpacity = rand.nextInt(StorageBlockTraits.OPAQUE);
		}

		if (traits.draw(Trait.SLIPPERY, rand))
		{
			this.slipperiness = StorageBlockTraits.DEFAULT_SLIPPERINESS + (rand.nextInt(5) / 10) - 0.2F;
		}

		if (traits.draw(Trait.ACCELERATING, rand))
		{
			this.acceleration = 1 + (rand.nextInt(11) / 10) - 0.5F;
		}

		if (traits.draw(Trait.WALK_DAMAGE, rand))
		{
			this.walkDamage = rand.nextInt(3) + 1;
		}

		if (traits.draw(Trait.FLAMMABLE, rand))
		{
			this.flammability = FlammabilityOf.getRandomFlammability(rand);
		}
	}

	protected void applyRole(VanillaRole role, Random rand, GenerationContext context)
	{
		switch (role)
		{
			case REDSTONE -> this.applyRedstone();
			case CURRENCY -> this.applyCurrency(rand, context);
			case FUEL -> this.applyFuel(rand);
			case BEACON -> this.applyBeacon();
			case ENCHANTING_CURRENCY -> this.applyEnchantingCurrency();
			case COAL -> this.setHarvestLevel(HarvestLevel.WOOD);
			case IRON -> this.applyMetalRole(HarvestLevel.STONE, 1);
			case GOLD -> this.applyMetalRole(HarvestLevel.IRON, 1);
			case DIAMOND -> this.applyMetalRole(HarvestLevel.IRON, 2);
			case BLUE_DYE ->
			{
				// The blue dye role only adds a recipe
			}
		}

		context.registerVariants(this.type(), role);
	}

	private void applyCurrency(Random rand, GenerationContext context)
	{
		this.rarity = this.rarity.next();
		LegacyVillagerTradeDraws.draw(rand, context);
	}

	private void applyRedstone()
	{
		this.rarity = this.rarity.next();
		this.powered = !this.oreDropsItself;
		this.redstonePower = REDSTONE_BLOCK_POWER;
	}

	private void applyFuel(Random rand)
	{
		int fuelTicks = FUEL_TICKS_PER_LEVEL * (rand.nextInt(10) + 1);
		this.itemFuelTicks = fuelTicks;
		this.blockFuelTicks = fuelTicks * BLOCK_FUEL_MULTIPLIER;
	}

	private void applyBeacon()
	{
		this.beaconPayment = true;
		this.beaconBase = true;
	}

	/**
	 * The 1.12 code multiplied the integer maximum by 2.5, which truncates the result.
	 */
	private void applyEnchantingCurrency()
	{
		this.rarity = this.rarity.next();

		if (!this.oreDropsItself)
		{
			this.minimumDrop *= 2;
			this.maximumDrop *= 2.5;
		}

		this.enchantingCurrency = true;
	}

	private void applyMetalRole(HarvestLevel harvestLevel, int rarityLevels)
	{
		this.setHarvestLevel(harvestLevel);

		for (int level = 0; level < rarityLevels; level++)
		{
			this.rarity = this.rarity.next();
		}
	}

	private void setHarvestLevel(HarvestLevel harvestLevel)
	{
		this.oreHarvestLevel = harvestLevel;
		this.blockHarvestLevel = harvestLevel;
	}

	protected Optional<MaterialDefinition> material()
	{
		return Optional.empty();
	}

	@Override
	public OreSetDefinition toDefinition(int index)
	{
		ItemTraits item = new ItemTraits(this.itemTextureId, this.color, this.shiny, this.itemFuelTicks, this.enchantingCurrency, this.firestarter, this.beaconPayment, Optional.ofNullable(this.food));
		StorageBlockTraits block = new StorageBlockTraits(this.blockTextureId, this.blockHarvestLevel, this.falls, this.absorbsWater, this.walkDamage, this.flammability, this.acceleration, this.beaconBase, this.redstonePower, this.blockFuelTicks, this.lightLevel, this.lightOpacity, this.slipperiness);
		OreTraits ore = new OreTraits(this.oreTextureId, this.oreHarvestLevel, this.oreDrop(), this.powered);

		return new OreSetDefinition(index, this.name, this.type(), this.rarity, this.roles, this.generation, item, block, ore, this.material());
	}

	private OreDrop oreDrop()
	{
		if (this.oreDropsItself)
		{
			return new OreDrop.Itself();
		}
		else
		{
			return new OreDrop.Items(this.minimumDrop, this.maximumDrop, this.minimumExperience, this.maximumExperience);
		}
	}
}
