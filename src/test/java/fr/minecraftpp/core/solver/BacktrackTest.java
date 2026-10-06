package fr.minecraftpp.core.solver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import fr.minecraftpp.core.GoldenFixture;
import fr.minecraftpp.core.GoldenFixture.GoldenSet;
import fr.minecraftpp.core.ore.OreProperties;
import fr.minecraftpp.core.solver.engine.Assignment;
import fr.minecraftpp.core.solver.engine.CSP;
import fr.minecraftpp.core.solver.engine.Network;
import fr.minecraftpp.core.solver.engine.constraint.Constraint;

class BacktrackTest
{
	private static final int NUMBER_OF_ORES = 7;

	@Test
	void solutionSatisfiesEveryConstraint() throws Exception
	{
		for (long seed = 0; seed < 100; seed++)
		{
			Random rand = new Random(seed);
			Network network = new Network(new Pretreatment(rand, NUMBER_OF_ORES).toString());
			Assignment solution = new CSP(network, rand).searchSolution();

			assertEquals(network.getVars().size(), solution.size(), "every variable is assigned, seed " + seed);

			for (Constraint constraint : network.getConstraints())
			{
				assertFalse(constraint.violation(solution), "constraint " + constraint + " violated, seed " + seed);
			}
		}
	}

	@Test
	void sameSeedGivesTheSameSolution()
	{
		for (long seed = 0; seed < 20; seed++)
		{
			assertEquals(Backtrack.generateSolution(new Random(seed), NUMBER_OF_ORES), Backtrack.generateSolution(new Random(seed), NUMBER_OF_ORES));
		}
	}

	@Test
	void everyOreReceivesAtLeastOneProperty()
	{
		for (long seed = 0; seed < 100; seed++)
		{
			Set<Integer> ores = new TreeSet<>(Backtrack.generateSolution(new Random(seed), NUMBER_OF_ORES).values());

			assertEquals(NUMBER_OF_ORES, ores.size(), "seed " + seed);
		}
	}

	@Test
	void rolesMatchThe112Generator()
	{
		for (long seed : GoldenFixture.SEEDS)
		{
			Map<Integer, List<OreProperties>> propertiesByOre = groupByOre(Backtrack.generateSolution(new Random(seed), NUMBER_OF_ORES));
			List<GoldenSet> goldenSets = GoldenFixture.load(seed).sets();

			int index = 0;
			for (List<OreProperties> properties : propertiesByOre.values())
			{
				GoldenSet goldenSet = goldenSets.get(index);
				assertEquals(goldenSet.type(), expectedSetType(properties), "set type, seed " + seed + ", set " + index);
				assertEquals(goldenRoles(goldenSet), expectedRoles(properties), "roles, seed " + seed + ", set " + index);
				index++;
			}
		}
	}

	private static Map<Integer, List<OreProperties>> groupByOre(Map<String, Integer> solution)
	{
		Map<Integer, List<OreProperties>> propertiesByOre = new TreeMap<>();

		for (Map.Entry<String, Integer> entry : solution.entrySet())
		{
			propertiesByOre.computeIfAbsent(entry.getValue(), ore -> new ArrayList<>()).add(OreProperties.fromString(entry.getKey()));
		}

		return propertiesByOre;
	}

	private static String expectedSetType(List<OreProperties> properties)
	{
		if (properties.contains(OreProperties.METAL))
		{
			return "MetalSet";
		}
		else if (properties.contains(OreProperties.MATERIAL))
		{
			return "MaterialSet";
		}
		else
		{
			return "SimpleSet";
		}
	}

	/**
	 * Roles as the 1.12 sets record them: material and metal sets ignore the currency role, metal sets also ignore blue dye and redstone.
	 */
	private static Set<String> expectedRoles(List<OreProperties> properties)
	{
		boolean isMetal = properties.contains(OreProperties.METAL);
		boolean isMaterial = isMetal || properties.contains(OreProperties.MATERIAL);
		Set<String> roles = new TreeSet<>();

		for (OreProperties property : properties)
		{
			switch (property)
			{
				case BLUEDYE -> addUnless(roles, isMetal, "BlueDye");
				case REDSTONE -> addUnless(roles, isMetal, "Redstone");
				case CURRENCY -> addUnless(roles, isMaterial, "Currency");
				case FUEL -> roles.add("Fuel");
				case BEACON -> roles.add("Beacon");
				case ENCHANT_CURRENCY -> roles.add("EnchantCurrency");
				case COAL -> roles.add("Coal");
				case IRON -> roles.add("Iron");
				case GOLD -> roles.add("Gold");
				case DIAMOND -> roles.add("Diamond");
				default ->
				{
				}
			}
		}

		return roles;
	}

	private static void addUnless(Set<String> roles, boolean ignored, String role)
	{
		if (!ignored)
		{
			roles.add(role);
		}
	}

	private static Set<String> goldenRoles(GoldenSet goldenSet)
	{
		String roles = goldenSet.value("roles");

		if (roles.isEmpty())
		{
			return new TreeSet<>();
		}
		else
		{
			return new TreeSet<>(List.of(roles.split(",")));
		}
	}
}
