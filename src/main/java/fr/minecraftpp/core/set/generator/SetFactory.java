package fr.minecraftpp.core.set.generator;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import fr.minecraftpp.core.ore.OreProperties;
import fr.minecraftpp.core.ore.OreRarity;
import fr.minecraftpp.core.set.OreGeneration;
import fr.minecraftpp.core.set.VanillaRole;

/**
 * Creates and constructs the generator of a set from the properties the solver gave to one ore.
 */
public final class SetFactory
{
	private SetFactory()
	{
	}

	/**
	 * Runs the construction pass of a set. As in 1.12, the ore rarity is drawn first, then the set is constructed, then the roles are assigned.
	 */
	public static OreSetGenerator generateSet(List<OreProperties> properties, Random rand, String name)
	{
		OreRarity oreRarity = OreRarity.getRarityFrom(properties, rand);
		OreGeneration generation = new OreGeneration(oreRarity.getVeinAmount(), oreRarity.getVeinDensity(), oreRarity.getMaxHeight());

		SimpleSetGenerator generator = createProperSet(properties, name, generation);
		generator.construct(rand);

		for (OreProperties property : properties)
		{
			toRole(property).ifPresent(generator::assignRole);
		}

		return generator;
	}

	private static SimpleSetGenerator createProperSet(List<OreProperties> properties, String name, OreGeneration generation)
	{
		if (properties.contains(OreProperties.METAL))
		{
			return new MetalSetGenerator(name, generation);
		}
		else if (properties.contains(OreProperties.MATERIAL))
		{
			return new MaterialSetGenerator(name, generation);
		}
		else
		{
			return new SimpleSetGenerator(name, generation);
		}
	}

	/**
	 * The role matching a solver property. MATERIAL and METAL choose the kind of set instead.
	 */
	private static Optional<VanillaRole> toRole(OreProperties property)
	{
		return switch (property)
		{
			case BLUEDYE -> Optional.of(VanillaRole.BLUE_DYE);
			case REDSTONE -> Optional.of(VanillaRole.REDSTONE);
			case CURRENCY -> Optional.of(VanillaRole.CURRENCY);
			case BEACON -> Optional.of(VanillaRole.BEACON);
			case FUEL -> Optional.of(VanillaRole.FUEL);
			case ENCHANT_CURRENCY -> Optional.of(VanillaRole.ENCHANTING_CURRENCY);
			case COAL -> Optional.of(VanillaRole.COAL);
			case IRON -> Optional.of(VanillaRole.IRON);
			case GOLD -> Optional.of(VanillaRole.GOLD);
			case DIAMOND -> Optional.of(VanillaRole.DIAMOND);
			case EMPTY, MATERIAL, METAL -> Optional.empty();
		};
	}
}
