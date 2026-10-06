package fr.minecraftpp.core.set.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import fr.minecraftpp.core.naming.NameGenerator;
import fr.minecraftpp.core.ore.OreProperties;
import fr.minecraftpp.core.set.OreCatalog;
import fr.minecraftpp.core.set.OreSetDefinition;
import fr.minecraftpp.core.solver.Backtrack;
import fr.minecraftpp.core.trait.TraitCatalog;

/**
 * Generates the seven sets of a seed, with the draw order of the 1.12 SetManager: the solver first, then the construction of every set, then the effects of every set.
 */
public final class OreCatalogGenerator
{
	public static final int NUMBER_OF_ORES = 7;

	private OreCatalogGenerator()
	{
	}

	public static OreCatalog generate(long seed, NameGenerator names, TraitCatalog traits)
	{
		Random rand = new Random(seed);
		Map<Integer, List<OreProperties>> propertiesByOre = groupByOre(Backtrack.generateSolution(rand, NUMBER_OF_ORES));

		List<OreSetGenerator> generators = new ArrayList<>();
		for (List<OreProperties> properties : propertiesByOre.values())
		{
			generators.add(SetFactory.generateSet(properties, rand, names.nextName()));
		}

		GenerationContext context = new GenerationContext();
		for (OreSetGenerator generator : generators)
		{
			generator.setupEffects(rand, traits, context);
		}

		List<OreSetDefinition> sets = new ArrayList<>();
		for (int index = 0; index < generators.size(); index++)
		{
			sets.add(generators.get(index).toDefinition(index));
		}

		return new OreCatalog(seed, sets);
	}

	/**
	 * Groups the solver variables by ore, in ore order. Within an ore, the properties keep the iteration order of the solution, as in 1.12.
	 */
	private static Map<Integer, List<OreProperties>> groupByOre(Map<String, Integer> solution)
	{
		Map<Integer, List<OreProperties>> propertiesByOre = new TreeMap<>();

		for (Map.Entry<String, Integer> entry : solution.entrySet())
		{
			propertiesByOre.computeIfAbsent(entry.getValue(), ore -> new ArrayList<>()).add(OreProperties.fromString(entry.getKey()));
		}

		return propertiesByOre;
	}
}
